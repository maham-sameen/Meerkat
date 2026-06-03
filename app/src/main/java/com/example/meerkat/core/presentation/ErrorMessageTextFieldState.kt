package com.example.meerkat.core.presentation

data class ErrorMessageTextFieldState(
    val text : String = "" ,
    val isError : Boolean = false
)



data class FocusedErrorTxtFieldState(
    val isFocus : Boolean  = false,
    val text : String = "" ,
    val isError : Boolean = false
)