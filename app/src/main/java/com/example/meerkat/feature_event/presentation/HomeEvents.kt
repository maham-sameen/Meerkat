package com.example.meerkat.feature_event.presentation

sealed class HomeEvents {

    object DismissDialog : HomeEvents()
    data class OnPermissionResult(
        val permission : String ,
        val isGranted : Boolean
    ) : HomeEvents()
}