package com.example.meerkat.feature_auth.presentation.intro

sealed class IntroEvents {
    object SignUp : IntroEvents()
    object SignIn : IntroEvents()
}