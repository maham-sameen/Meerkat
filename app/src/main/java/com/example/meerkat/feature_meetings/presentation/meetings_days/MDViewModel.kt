package com.example.meerkat.feature_meetings.presentation.meetings_days

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meerkat.core.calendar.domain.model.SessionExpiredException
import com.example.meerkat.core.time_zone.domain.util.TimeZoneCategory
import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import com.example.meerkat.feature_meetings.domain.use_case.meeting_days.MeetingsUseCases
import com.example.meerkat.feature_meetings.domain.util.DaysOfWeek
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_meetings.domain.util.MeetingDate
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import com.example.meerkat.feature_meetings.domain.util.insertSorted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MDViewModel
    @Inject
    constructor(
        val meetingsUseCases: MeetingsUseCases
    ) : ViewModel() {
        val days = mutableStateListOf<MeetingDate>().apply {
            addAll(meetingsUseCases.getMeetingDates(LocalDate.now()))
        }


      private var  _currentSelectedDateIndex  =
          mutableIntStateOf(meetingsUseCases.getMeetingDates.currentSelectedIndex)
      val currentSelectedDateIndex : State<Int> = _currentSelectedDateIndex

      private var _selectedMeetingDate  =
          mutableStateOf<MeetingDate>(days[meetingsUseCases.getMeetingDates.currentSelectedIndex])
      val selectedOption : State<MeetingDate> = _selectedMeetingDate

      private  var _currentWeek  = mutableStateOf<DaysOfWeek>(
          _selectedMeetingDate.value.daysOfWeek ?: meetingsUseCases.getMeetingDates.getDaysOfWeek(
              LocalDate.now()
          )
      )
      val currentWeek : State<DaysOfWeek> = _currentWeek



     // time slots
      val currentTimeSlots = mutableStateListOf<TimeSlot>().apply {
          addAll(emptyList())
      }

      private var _selectedTimeSlots : MutableState<TimeSlot> = mutableStateOf(
          TimeSlot(
              label = "",
              startTime = ZonedDateTime.now(ZoneId.of("UTC")),
              endTime = ZonedDateTime.now(ZoneId.of("UTC")).plusMinutes(30),
              duration = Duration.ofMinutes(30),
              isAvailable = true
          )
      )
      val selectedTimeSots : State<TimeSlot> = _selectedTimeSlots


    // Time zone
    private val timeZonesCategoryList = mutableListOf<TimeZoneCategory>()
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private var _selectedTimeZone  =
        mutableStateOf<TimeZoneInfo>(TimeZoneInfo(name = "Karachi", zoneName = "Asia/Karachi"))
    val selectedTimeZone : State<TimeZoneInfo> = _selectedTimeZone

    private val _timeZonesCategory = MutableStateFlow(timeZonesCategoryList)


    private var _currentTimeZone =
        mutableStateOf<TimeZoneInfo>(TimeZoneInfo(name = "Karachi", zoneName = "Asia/Karachi"))
    val currentTimeZone : State<TimeZoneInfo> = _currentTimeZone



    private var _showCustomizeEvent = mutableStateOf(false)
    val showCustomizeEvent : State<Boolean> = _showCustomizeEvent

    private var _host  = MutableStateFlow<MeetingAttendee?>(null)
    val host : StateFlow<MeetingAttendee?> = _host


    private var _sessionDialogState = mutableStateOf(false)
    val sessionDialogState : State<Boolean> = _sessionDialogState




    private var _eventId : MutableState<Long?>  = mutableStateOf(null)

    init {
        timeZonesCategoryList.apply {   addAll(meetingsUseCases.getTimeZonesUseCase.invoke()) }// Reassign to ensure a fresh list
        _timeZonesCategory.update { timeZonesCategoryList } // Directly set a new list to trigger an updateAs
        viewModelScope.launch {
            launch { setHost() }
            launch{ setUpTimeSlots() }
            launch { setTimeZone() }
        }
    }


    suspend fun setHost() {
        val hostResult = meetingsUseCases.getHost()
        hostResult.fold(
            onSuccess = {
                _host.value = it
            },
            onFailure = {
                if(it is SessionExpiredException){
                    _sessionDialogState.value = true
                }
            }
        )
    }

    suspend fun setTimeZone(){
        val timeZoneResult = meetingsUseCases.getTZCalender()
        timeZoneResult.fold(
            onSuccess = {
                _currentTimeZone.value = TimeZoneInfo(
                    name = it,
                    zoneName = it
                )
            },
            onFailure = {
                if(it is SessionExpiredException){
                    _sessionDialogState.value = true
                }
            }
        )
    }

    @OptIn(FlowPreview::class)
    val timeZonesCategory = searchText
        .debounce(300L)
        .onEach { _isSearching.update { true } }
        .combine(_timeZonesCategory) { text, tzs ->
            if(text.isBlank()) {
                tzs
            } else {
                delay(2000L)
                tzs.mapNotNull { it.doesMatchSearchQuery(text) }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            _timeZonesCategory.value
        )



    fun onEvent(event: MeetingsDaysEvents){
        when(event){
            is MeetingsDaysEvents.SelectADay -> {
                onToggleSelection(event.index)
            }
            is MeetingsDaysEvents.SelectNewDate -> {
                onNewDateSelected(event.data)
            }
            is MeetingsDaysEvents.SelectTimeSlot -> {
                _showCustomizeEvent.value = true
                onSelectTimeZone(event.index)
            }
            MeetingsDaysEvents.OnDone -> {
                _currentTimeZone.value = _selectedTimeZone.value
            }
            is MeetingsDaysEvents.OnSearchTxt -> {
                _searchText.value = event.text
            }
            is MeetingsDaysEvents.OnToggleSelection -> {
                _selectedTimeZone.value = event.selectedOption
            }

            MeetingsDaysEvents.DismissCustomizeEvent -> {
                _showCustomizeEvent.value = false
            }

            MeetingsDaysEvents.DismissDialog -> {
                _sessionDialogState.value = false
            }
            MeetingsDaysEvents.ShowDialog -> {
                _sessionDialogState.value = true
            }
        }
    }

    private fun onToggleSelection(index : Int){
        println("onToggleSelection")
        val item = days[index]
        _selectedMeetingDate.value = item
        _currentSelectedDateIndex.intValue = index
        if(item.daysOfWeek!= null &&_currentWeek.value != item.daysOfWeek){
            _currentWeek.value = item.daysOfWeek
        }
        println("currentSelectedDateIndex : ${_currentSelectedDateIndex.intValue} " +
                "\n selectedMeetingDt : ${_selectedMeetingDate.value}" +
                "\n currentWeek : ${_currentWeek.value}")
        viewModelScope.launch{ setUpTimeSlots() }
        println("currentSelectedDateIndex : ${_currentSelectedDateIndex.intValue} " +
                "\n selectedMeetingDt : ${_selectedMeetingDate.value}" +
                "\n currentWeek : ${_currentWeek.value}")
    }

    private fun onNewDateSelected(date : LocalDate){
        println("OnNewDateSelected : data $date")
        days.clear()
        days.apply {
            addAll(meetingsUseCases.getMeetingDates(date))
        }
        _currentSelectedDateIndex.intValue = meetingsUseCases.getMeetingDates.currentSelectedIndex
        _selectedMeetingDate.value = days[_currentSelectedDateIndex.intValue]
        _currentWeek.value = _selectedMeetingDate.value.daysOfWeek!!
        println("currentSelectedDateIndex : ${_currentSelectedDateIndex.intValue} " +
                "\n selectedMeetingDt : ${_selectedMeetingDate.value}" +
                "\n currentWeek : ${_currentWeek.value}")
    }


    private fun onSelectTimeZone(index: Int){
        val item = currentTimeSlots[index]
        _selectedTimeSlots.value = item
    }


    private suspend fun setUpTimeSlots() {
        val timeSlots = meetingsUseCases.getBusyTimeList(
            meetingDate = _selectedMeetingDate.value,
            onSessionExpired = {
                _sessionDialogState.value = true
            },
            timeZone = _currentTimeZone.value.zoneName
        )

        currentTimeSlots.clear()
        currentTimeSlots.apply {
            addAll(timeSlots)
        }
        _eventId.value?.let {
            // add the time slot of the meeting to update
            currentTimeSlots.insertSorted(_selectedTimeSlots.value)
        }
    }


     fun toUpdateMeetings(eventId : Long?){
        _eventId.value= eventId
        viewModelScope.launch {
            _eventId.value?.let{id ->
                meetingsUseCases.toUpdateMeetings(id)?.let {
                    onNewDateSelected(it.startTime.toLocalDate())
                    _selectedTimeSlots.value = it
                    _showCustomizeEvent.value = true
                    _selectedTimeZone.value = TimeZoneInfo(
                        name = it.startTime.zone.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        zoneName = it.startTime.zone.toString()
                    )
                    setUpTimeSlots()
                }
            }
        }
    }
}
/*
 println("firstTimeSlot : $firstTimeSlot")

 */