package com.example.meerkat.feature_meetings.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meerkat.Constants
import com.example.meerkat.core.presentation.UIState
import com.example.meerkat.feature_meetings.domain.use_case.GetTZCalender
import com.example.meerkat.feature_meetings.domain.use_case.meeting_snap_shots.DeleteMeetingSnapshot
import com.example.meerkat.feature_meetings.domain.use_case.meeting_snap_shots.GetMeetingSnapShots
import com.example.meerkat.feature_meetings.domain.util.MeetingSnapshot
import com.example.meerkat.feature_meetings.presentation.meeting_snap_shots.MeetingSnapshotsEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.emptyList


@HiltViewModel
class MeetingScrViewModel
@Inject constructor(
    private val getTZCalender: GetTZCalender ,
    private val getMeetingSnapShots: GetMeetingSnapShots ,
    private val deleteMeetingSnapshot: DeleteMeetingSnapshot
): ViewModel() {

    private var _sessionDialogState = mutableStateOf(false)
    val sessionDialogState : State<Boolean> = _sessionDialogState
    private var _cancelMeetingDialogState = mutableStateOf(false)
    val cancelMeetingDialogState : State<Boolean> = _cancelMeetingDialogState
    private var _uiState = MutableSharedFlow<UIState>()
    val uiState : SharedFlow<UIState> = _uiState
    private var timeZone = MutableStateFlow("")
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalCoroutinesApi::class)
    val meetingSnapshots : StateFlow<List<MeetingSnapshot>> = timeZone
        .filter {
            Timber.i("timeZone filter $it ${Constants.appName}")
            it.isNotEmpty()
        }
        .flatMapLatest {
            Timber.i("timeZone flatMapLatest $it ${Constants.appName} ")
            getMeetingSnapShots(it)
        }.stateIn(viewModelScope , SharingStarted.WhileSubscribed(500) , emptyList())

    private var selectedMeetingSnapshot : MeetingSnapshot? = null

    init {
        viewModelScope.launch {
            getTZCalender()
                .fold(
                    onSuccess = {
                        Timber.i("Get TimeZone Success $it ${Constants.appName}")
                        timeZone.value = it
                    },
                    onFailure = {
                        Timber.i("Get TimeZone failure ${it.toString()} ${Constants.appName}")
                        _uiState.emit(UIState.Error(it.message.toString()))
                    }
                )
        }
    }

    fun onEvent(event : MeetingSnapshotsEvents){
        when(event){
            MeetingSnapshotsEvents.ConfirmCancelMeeting -> {
                Timber.i("ConfirmCancelMeeting , ${Constants.appName}")
                viewModelScope.launch {
                    _uiState.emit(UIState.Loading)
                    selectedMeetingSnapshot?.let {
                        deleteMeetingSnapshot(it.eventId).fold(
                            onSuccess = {
                                Timber.i("Meeting has been deleted successfully , ${Constants.appName}")
                                _cancelMeetingDialogState.value = false
                                _uiState.emit(UIState.Success("Meeting has deleted successfully"))
                            },
                            onFailure = {
                                Timber.i("Meeting has not been deleted , ${Constants.appName}")
                                _cancelMeetingDialogState.value = false
                                _uiState.emit(UIState.Error(it.message.toString()))
                            }
                        )
                    }
                }
            }
            is MeetingSnapshotsEvents.ShowCancelMeetingDialogState -> {
                Timber.i("ShowCancelMeetingDialogState , ${Constants.appName}")
                selectedMeetingSnapshot = event.meetingSnapshot
                _cancelMeetingDialogState.value = true
            }
            is MeetingSnapshotsEvents.OnDismissCancelMeetingDialogState -> {
                Timber.i("OnDismissCancelMeetingDialogState , ${Constants.appName}")
                _cancelMeetingDialogState.value = false
            }
            is MeetingSnapshotsEvents.OnDismissSessionDialogState -> {
                Timber.i("OnDismissSessionDialogState , ${Constants.appName}")
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