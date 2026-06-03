package com.example.meerkat.feature_event.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
    @Inject constructor(

    ): ViewModel(){
        private var _showEventType = mutableStateOf(false)
        val showEntriesType : State<Boolean> = _showEventType

       val visiblePermissionDialogStack = mutableStateListOf<String>()

    fun onEvent(events: HomeEvents){
        when(events){
           HomeEvents.DismissDialog -> {
                visiblePermissionDialogStack.removeAt(visiblePermissionDialogStack.size -1)
            }
            is HomeEvents.OnPermissionResult -> {
                if(!events.isGranted && !visiblePermissionDialogStack.contains(events.permission)){
                    visiblePermissionDialogStack.add(visiblePermissionDialogStack.size  , events.permission)
                }
            }
        }
    }
}