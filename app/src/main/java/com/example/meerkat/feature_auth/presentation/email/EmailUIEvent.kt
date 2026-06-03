package com.example.meerkat.feature_auth.presentation.email

sealed class EmailUIEvent {
    data class EnterEmail(val email : String) : EmailUIEvent()
    data class EnterName(val name : String) : EmailUIEvent()
    data class EnterPassword(val password : String) : EmailUIEvent()
    object OnToggleVisibility : EmailUIEvent()
    object AuthWithEmail  : EmailUIEvent()
    data class IntroTxt(val id : Int) : EmailUIEvent()

}