package com.example.meerkat.feature_auth.presentation.util

import com.example.meerkat.feature_auth.presentation.auth.AuthLangState
import com.example.meerkat.feature_auth.presentation.intro.IntroEvents

object  AuthConstant {
    val englishSignIn = AuthLangState(
        introEvents = IntroEvents.SignIn,
       enterIntoCalendlyAccount  = "Log into your Calendly account",
      enterEmail  = "Enter your email to get started",
      withGoogle  = "Log in with Google" ,
      withMicrosoft  = "Log in with Microsoft" ,
       withOrNoAccount = "Don `t have an account" ,
       auth  = "Sign up" ,
    )
   val englishSignUp = AuthLangState(
       introEvents = IntroEvents.SignUp,
       enterIntoCalendlyAccount  = "Sign up with your Calendly account",
      enterEmail  = "Enter your email to get started",
      withGoogle  = "Sign up with Google" ,
      withMicrosoft  = "Sign up with Microsoft" ,
       withOrNoAccount = "Already have an account" ,
       auth  = "Log in"
    )
    // whole document
    const val USERS = "users"

}

