package com.example.meerkat.feature_auth.presentation.auth

import com.example.meerkat.feature_auth.presentation.intro.IntroEvents

data class AuthLangState(
    val introEvents: IntroEvents = IntroEvents.SignUp,
    val enterIntoCalendlyAccount : String = "Log into your Calendly account",
    val enterEmail : String  = "Enter your email to get started",
    val withGoogle : String = "Log in with Google",
    val withMicrosoft : String = "Log in with Microsoft",
    val withOrNoAccount : String = "Don `t have an account",
    val auth : String = "Sign up",
    val continueText : String = "Continue",
    val or : String = "OR",
    val emailAddress : String = "Email Address"
)