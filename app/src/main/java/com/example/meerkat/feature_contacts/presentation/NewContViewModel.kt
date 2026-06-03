package com.example.meerkat.feature_contacts.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meerkat.core.calendar.domain.model.SessionExpiredException
import com.example.meerkat.core.presentation.FocusedErrorTxtFieldState
import com.example.meerkat.core.presentation.isValidEmail
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.domain.model.PhoneNumber
import com.example.meerkat.feature_contacts.domain.use_case.ContactUseCases
import com.example.meerkat.core.time_zone.domain.util.TimeZoneCategory
import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class NewContViewModel
    @Inject
    constructor(private val contactUseCases: ContactUseCases)
    : ViewModel(){

        private var _nameTxtFieldState  = mutableStateOf(FocusedErrorTxtFieldState())
        val nameTxtFieldState : State<FocusedErrorTxtFieldState> = _nameTxtFieldState


       private var _emailTxtFieldState = mutableStateOf(FocusedErrorTxtFieldState())
       val emailTxtFieldState : State<FocusedErrorTxtFieldState> = _emailTxtFieldState

       private var _phoneNoTxtFieldState = mutableStateOf(PhoneNoState())
       val phoneNoTxtFieldState : State<PhoneNoState> = _phoneNoTxtFieldState


       private var timeZonesCategoryList = mutableListOf<TimeZoneCategory>()

        private val _searchText = MutableStateFlow("")
        val searchText = _searchText.asStateFlow()
        private var _selectedOption  = mutableStateOf<TimeZoneInfo>(TimeZoneInfo(name = "Karachi" , zoneName = "Asia/Karachi"))
        val selectedOption : State<TimeZoneInfo> = _selectedOption
        private val _timeZoneTxtFieldState  = mutableStateOf<FocusedErrorTxtFieldState>(
            FocusedErrorTxtFieldState())
        val timeZoneTxtFieldState : State<FocusedErrorTxtFieldState> = _timeZoneTxtFieldState
        private val _timeZonesCategory = MutableStateFlow(timeZonesCategoryList)

    private val _currentUserId = MutableStateFlow("")


    private var _isContactSearching = MutableStateFlow(false)


    private var _isTimeZoneSearching = MutableStateFlow(false)
    val isTimeZoneSearching  = _isTimeZoneSearching.asStateFlow()

    val isSearching = derivedStateOf {
        _isContactSearching.value || _isTimeZoneSearching.value
    }

    val isSaveAndContinue = derivedStateOf {
           nameTxtFieldState.value.text.isNotBlank() &&
                   emailTxtFieldState.value.text.isNotBlank() &&
                   emailTxtFieldState.value.text.isValidEmail() &&
                   phoneNoTxtFieldState.value.phoneNoTxtFieldState.text.isNotBlank() &&
                   timeZoneTxtFieldState.value.text.isNotBlank() &&
                   !phoneNoTxtFieldState.value.phoneNoTxtFieldState.isError &&
                   _currentUserId.value.isNotBlank()
       }

    @OptIn(FlowPreview::class)
    val timeZonesCategory = searchText
        .debounce(1000L)
        .onEach { _isTimeZoneSearching.update { true } }
        .combine(_timeZonesCategory) { text, tzs ->
            if(text.isBlank()) {
                tzs
            } else {
                tzs.mapNotNull { it.doesMatchSearchQuery(text) }
            }
        }
        .onEach { _isTimeZoneSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _timeZonesCategory.value
        )


    @OptIn(ExperimentalCoroutinesApi::class)
    val allContacts : StateFlow<List<ContactEntity>> = _currentUserId
        .onStart { _isContactSearching.update { true } }
        .filter { it.isNotEmpty() }
        .flatMapLatest { userId ->
            contactUseCases.getAllContacts(userId)
                .catch { e ->
                    if (e is SessionExpiredException) {
                        _sessionDialogState.value = true
                    }
                }
        }
        .onCompletion { _isContactSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    private var _sessionDialogState = mutableStateOf(false)
    val sessionDialogState : State<Boolean> = _sessionDialogState

    init {
        timeZonesCategoryList.apply {   addAll(contactUseCases.getTimeZonesUseCase.invoke()) }// Reassign to ensure a fresh list
        _timeZonesCategory.update { timeZonesCategoryList } // Directly set a new list to trigger an updateAs
        viewModelScope.launch {
           val userIdResult =  contactUseCases.getCurrentUserId()
            userIdResult.fold(
                onSuccess = {
                    _currentUserId.value = it
                },
                onFailure = {
                    _sessionDialogState.value = true
                }
            )
        }
    }

    fun onEvent(event: NewContactEvents){
        when(event){
            is NewContactEvents.EnterName -> {
                _nameTxtFieldState.value = _nameTxtFieldState.value.copy(
                        text = event.name,
                        isError = event.name.isEmpty()
                )
            }
            is NewContactEvents.FocusChangeNameTxtField -> {
                _nameTxtFieldState.value = _nameTxtFieldState.value.copy(
                    isFocus = event.focusState.isFocused,
                    isError = event.focusState.isFocused && _nameTxtFieldState.value.text.isEmpty()
                )
            }

            is NewContactEvents.EnterEmail -> {
                _emailTxtFieldState.value = _emailTxtFieldState.value.copy(
                            text = event.email ,
                            isError = !event.email.isValidEmail()
                )
            }
            is NewContactEvents.EnterPhoneNo -> {
                val txtFieldState = _phoneNoTxtFieldState.value.phoneNoTxtFieldState
                _phoneNoTxtFieldState.value = _phoneNoTxtFieldState.value.copy(
                    code = event.code ,
                    phoneNoTxtFieldState = txtFieldState.copy(
                        text = event.phoneNo,
                        isError =  !event.isValid
                    )
                )
            }

            is NewContactEvents.FocusChangePhoneNoTxtField -> {
                val txtFieldState = _phoneNoTxtFieldState.value.phoneNoTxtFieldState
                _phoneNoTxtFieldState.value = _phoneNoTxtFieldState.value.copy(
                    phoneNoTxtFieldState = txtFieldState.copy(
                       isFocus = event.focusState.isFocused
                    )
                )
            }

            is NewContactEvents.FocusChangeEmailTxtField -> {
                _emailTxtFieldState.value = _emailTxtFieldState.value.copy(
                    isFocus = event.focusState.isFocused
                )
            }

            is NewContactEvents.OnToggleSelection -> {
                _selectedOption.value = event.selectedOption
            }

            is NewContactEvents.OnSearchTxt -> {
                _searchText.value = event.text
            }
            NewContactEvents.OnDone -> {
                _timeZoneTxtFieldState.value = _timeZoneTxtFieldState.value.copy(
                    text = _selectedOption.value.name
                )
            }

            is NewContactEvents.FocusChangeTimeZoneTxtField -> {
                _timeZoneTxtFieldState.value = _timeZoneTxtFieldState.value.copy(
                    isFocus = event.focusState.isFocused
                )
            }

            is NewContactEvents.EnterTimeZone -> {
                _timeZoneTxtFieldState.value = _timeZoneTxtFieldState.value.copy(
                    isError = event.text.isEmpty()
                )
            }

            NewContactEvents.OnSaveAndContinue -> {
                if(isSaveAndContinue.value){
                    viewModelScope.launch {
                        println("onSaveAndContinue of NewContViewModel")
                       contactUseCases.insertNewContact(
                           ContactEntity(
                               name = nameTxtFieldState.value.text,
                               email = emailTxtFieldState.value.text,
                               phoneNumber = PhoneNumber(
                                   countryCode = phoneNoTxtFieldState.value.code,
                                   phoneNumber = phoneNoTxtFieldState.value.phoneNoTxtFieldState.text
                               ),
                               timeZoneInfo = _selectedOption.value,
                               userId = _currentUserId.value ,
                           )
                       )
                    }
                }
            }

            is NewContactEvents.EditContact -> {
                setUpEditContactScreen(event)
            }

            NewContactEvents.DismissDialog -> {
                _sessionDialogState.value = false
            }
            NewContactEvents.ShowDialog -> {
                _sessionDialogState.value = true
            }
        }
    }


    private fun setUpEditContactScreen(event: NewContactEvents.EditContact){
        _nameTxtFieldState.value = FocusedErrorTxtFieldState(
            isFocus = true,
            text = event.contactEntity.name,
            isError = false
        )
        _emailTxtFieldState.value = FocusedErrorTxtFieldState(
            isFocus = true,
            text = event.contactEntity.email,
            isError = false
        )
        _phoneNoTxtFieldState.value = PhoneNoState(
            code = event.contactEntity.phoneNumber.countryCode,
            phoneNoTxtFieldState = FocusedErrorTxtFieldState(
                isFocus = true,
                text = event.contactEntity.phoneNumber.phoneNumber,
                isError = false
            )
        )
        _timeZoneTxtFieldState.value = FocusedErrorTxtFieldState(
            isFocus = false,
            text = event.contactEntity.timeZoneInfo.name,
            isError = false
        )
        _selectedOption.value = event.contactEntity.timeZoneInfo
    }

     fun clearState(){
        _nameTxtFieldState.value = FocusedErrorTxtFieldState()
        _emailTxtFieldState.value = FocusedErrorTxtFieldState()
        _phoneNoTxtFieldState.value = PhoneNoState()
        _timeZoneTxtFieldState.value = FocusedErrorTxtFieldState()
    }
}