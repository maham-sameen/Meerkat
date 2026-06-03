package com.example.meerkat.feature_auth.presentation.util

import kotlinx.serialization.Serializable


@Serializable
sealed class AuthScreen{

    @Serializable
    object IntroScr : AuthScreen()

    @Serializable
    object AuthScr : AuthScreen()

    @Serializable
    data class EmailScr(
        val emailTxt: String,
        val currentScreen: Int
    ): AuthScreen()
}
