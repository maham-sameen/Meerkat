package com.example.meerkat.feature_meetings.presentation.confirm_meeting

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.meerkat.Constants
import com.example.meerkat.core.presentation.UIState
import com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting.ConfirmMeetingUseCase
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.collections.map
import javax.inject.Inject


@HiltViewModel
class ConfirmMeetingViewModel
    @Inject constructor(
        private val confirmMeetingUseCase: ConfirmMeetingUseCase,
        private val notificationBuilder: NotificationCompat.Builder,
        private val notificationManager: NotificationManagerCompat ,
        private val workManager: WorkManager
    ): ViewModel() {

    // -------------------- Time Details --------------------

    private var _timeDetail = mutableStateOf("" to "")
    val timeDetail : State<Pair<String , String>> = _timeDetail

    // -------------------- Host --------------------

    private val _host = MutableStateFlow<UserAttendee?>(null)
    val host: StateFlow<UserAttendee?> = _host

    // -------------------- Search --------------------

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    @OptIn(ExperimentalCoroutinesApi::class)
    var _allContacts: MutableStateFlow<List<ContactAttendee>> = MutableStateFlow(emptyList())

    val filteredContacts : StateFlow<List<ContactAttendee>> = searchText
        .combine(_allContacts){ query, contacts ->
            if (query.isBlank()) contacts
            else contacts.filter { it.contact.name.contains(query, true) || it.contact.email.contains(query, true) }

        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _allContacts.value
        )
    // -------------------- Selected Guests --------------------

    private val _selectedGuest = MutableStateFlow<List<ContactAttendee>>(emptyList())
    val selectedGuest: StateFlow<List<ContactAttendee>> = _selectedGuest

    // -------------------- UI State --------------------

    private val _uiState : MutableStateFlow<UIState> = MutableStateFlow(UIState.Idle)
    val uiState: StateFlow<UIState> = _uiState

    // -------------------- Session Dialog --------------------

    private val _sessionDialogState = mutableStateOf(false)
    val sessionDialogState : State<Boolean> = _sessionDialogState

    private var eventId : Long? = null
    private var timeSlot : TimeSlot? = null


    init {
        viewModelScope.launch {
            // Load Host
            val user = confirmMeetingUseCase.getCurrentUser().fold(
                onSuccess = {
                    Timber.i("currentUser ${it.toString()} , ${Constants.appName}")
                    it
                            },
                onFailure = {
                    Timber.i("currentUser ${it.toString()} , ${Constants.appName}")
                    null }
            )
            user?.let {
                Timber.i("currentUser ${it.toString()} , ${Constants.appName}")
                _host.value = UserAttendee(
                    user = it,
                    attendee = confirmMeetingUseCase.mapUserToMeetingAttendee(it, true)
                )
                Timber.i("host : ${_host.toString()} , ${Constants.appName}")
            }

            // Load all contacts reactively whenever host changes
            _host.filterNotNull().collectLatest { host ->
                confirmMeetingUseCase.getAllContacts(host.user.id).collectLatest { contacts ->
                    _allContacts.value = contacts.map { contact ->
                        Timber.i("contact : $contact , ${Constants.appName}")
                        ContactAttendee(
                            contact = contact,
                            attendee = confirmMeetingUseCase.mapContactToAttendee(contact)
                        )
                    }
                }
            }
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event : ConfirmMeetingEvents){
        when(event){
            is ConfirmMeetingEvents.ToUpdateConfirmMeeting -> {
                Timber.i("ToUpdateConfirmMeeting , eventId : $eventId , ${Constants.appName}")
                eventId = event.eventId
                viewModelScope.launch{
                    confirmMeetingUseCase.getToUpdateConfirmMeeting(event.eventId).collectLatest {
                        Timber.i("allContacts : ${it.toString()} , ${Constants.appName}")
                        _selectedGuest.value = it
                    }
                }
            }
            is ConfirmMeetingEvents.ScheduleMeeting -> {
                if(_selectedGuest.value.isEmpty()){
                    viewModelScope.launch {
                        _uiState.value = UIState.Error("Please select at least one guest")
                        return@launch
                    }
                    return
                }
                viewModelScope.launch {
                    _uiState.value = UIState.Loading
                    Timber.i("ScheduleMeeting , ${Constants.appName}")
                    confirmMeetingUseCase.onScheduleMeeting(
                        existedEventId = eventId,
                        timeSlot = timeSlot!!,
                        description = getDescription(),
                        contactAttendee = _selectedGuest.value,
                        workManager = workManager
                    ).fold(
                        onSuccess = {
                            Timber.i("ScheduleMeeting Success , ${Constants.appName}")
                            _uiState.value = UIState.Success()
                        },
                        onFailure = {
                            Timber.i("ScheduleMeeting Failure , ${Constants.appName}")
                            _uiState.value = UIState.Error(it.message.toString())
                        }
                    )
                }
            }
            is ConfirmMeetingEvents.OnDismiss -> { _sessionDialogState.value = false }
            is ConfirmMeetingEvents.OnSearchText -> {
                _searchText.value = event.text
            }
            is ConfirmMeetingEvents.OnSelectGuest -> {
                if(!_selectedGuest.value.contains(event.contactAttendee)){
                    _selectedGuest.value += event.contactAttendee
                }
            }
            is ConfirmMeetingEvents.SetTimeDetail -> {
                timeSlot = event.slot
                _timeDetail.value = confirmMeetingUseCase.formateTimeSlotDetailed(event.slot)
            }
            is ConfirmMeetingEvents.OnUnSelectGuest -> {
                if(_selectedGuest.value.contains(event.contactAttendee)){
                    _selectedGuest.value -= event.contactAttendee
                }
            }
            is ConfirmMeetingEvents.OnSearchTextDone -> {
                val contactAttendee = _allContacts.value.firstOrNull {
                    it.contact.name.contains(_searchText.value, true) ||
                            it.contact.email.contains(_searchText.value, true)
                } ?: return
                if (!_selectedGuest.value.contains(contactAttendee)) {
                    _selectedGuest.value += contactAttendee
                }
            }
        }
    }

    private fun getDescription() : String{
        val hostName = _host.value?.user?.name ?: "You"
        val guestNames = _selectedGuest.value.map { it.contact.name }

        return if (guestNames.isEmpty()) {
            "$hostName has a meeting scheduled"
        } else {
            // join all but last with commas, add "and" before last
            val guestsText = guestNames.joinToString(
                separator = ", ",
                transform = { it }
            ).replace(", (?=[^,]*$)".toRegex(), " and ")
            "$hostName, you have a meeting with $guestsText"
        }
    }


    fun clearUiState(){
        viewModelScope.launch {
            _uiState.value = UIState.Idle
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification() {
        val  title: String = "Meeting Scheduled"
        val notification = notificationBuilder
            .setContentTitle(title)
            .setContentText(getDescription())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

}