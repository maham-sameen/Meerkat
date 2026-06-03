package com.example.meerkat.feature_meetings.presentation.schedule_meeting_details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meerkat.core.presentation.UIState
import com.example.meerkat.feature_meetings.domain.use_case.GetHost
import com.example.meerkat.feature_meetings.domain.use_case.scheduled_meetings_details.GetMeetingDetails
import com.example.meerkat.feature_meetings.domain.util.AMeetingDetails
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MeetingDetailsViewModel
    @Inject constructor(
        private val getMeetingDetails: GetMeetingDetails ,
        private val getHost : GetHost
    ): ViewModel() {
    private var _currentMeetingDetails = MutableStateFlow<AMeetingDetails>(AMeetingDetails())
    val currentMeetingDetails : StateFlow<AMeetingDetails> = _currentMeetingDetails
    private var _host  = MutableStateFlow<MeetingAttendee>(MeetingAttendee(
        profileText = "Y",
        name = "you",
        isHost = true
    ))
    val host : StateFlow<MeetingAttendee> = _host
    private var _uiState = MutableSharedFlow<UIState>()
    val uiState : SharedFlow<UIState> = _uiState
    private var _sessionDialogState = MutableStateFlow<Boolean>(false)
    val sessionDialogState : StateFlow<Boolean> = _sessionDialogState


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event : ScheduleMeetingDetailsEvents){
        when(event){
            is ScheduleMeetingDetailsEvents.OnEventId -> {
                viewModelScope.launch {
                    _uiState.emit(UIState.Loading)
                    getHost().fold(
                        onSuccess = { host ->
                            _host.value = host
                            getMeetingDetails(
                                eventId = event.eventId,
                                host = host
                            ).filterNotNull().collectLatest {
                                _currentMeetingDetails.value = it
                                _uiState.emit(UIState.Idle)
                            }
                        },
                        onFailure = {
                            _uiState.emit(UIState.Error(it.message.toString()))
                        }
                    )
                }
            }
            is ScheduleMeetingDetailsEvents.ShowDialog -> {
                _sessionDialogState.value = true
            }
            is ScheduleMeetingDetailsEvents.DismissDialog -> {
                _sessionDialogState.value = false
            }
        }
    }

    fun clearUiState(){
        viewModelScope.launch {
            _uiState.emit(UIState.Idle)
        }
    }
}