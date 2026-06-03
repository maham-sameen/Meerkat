package com.example.meerkat.feature_notification.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.meerkat.feature_notification.domain.use_case.NotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotifViewModel
    @Inject constructor(
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {


    private var _sessionDialogState = mutableStateOf(false)
    val sessionDialogState : State<Boolean> = _sessionDialogState

    private var timeZone = mutableStateOf("UTC")


    fun onEvent(event : NotificationsEvents){
        when(event){
            NotificationsEvents.DismissDialog -> {
                _sessionDialogState.value = false
            }

            NotificationsEvents.ShowDialog -> {
                _sessionDialogState.value = true
            }
        }
    }

    init {
        viewModelScope.launch {
            notificationUseCase.getTimeZoneUseCase()
                .fold(
                    onSuccess = {timeZone.value = it},
                    onFailure = {}
                )
        }
    }


    val notificationEvents = notificationUseCase
        .getNotificationEvents(timeZone.value)
        .cachedIn(viewModelScope)

}