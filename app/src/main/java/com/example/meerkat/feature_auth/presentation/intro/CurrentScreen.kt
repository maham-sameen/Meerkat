package com.example.meerkat.feature_auth.presentation.intro

sealed class CurrentScreen {
     object SIGN_UP : CurrentScreen()
     object SIGN_IN : CurrentScreen()
}