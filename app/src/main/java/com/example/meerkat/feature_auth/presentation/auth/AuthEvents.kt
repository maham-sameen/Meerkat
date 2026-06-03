package com.example.meerkat.feature_auth.presentation.auth

import android.content.Context
import android.content.Intent
import com.example.meerkat.MainActivity

sealed class AuthEvents {
    data class EnterEmailAddress(val value : String) : AuthEvents()
    object EmailAddressValidation: AuthEvents()
    data class StartWithGoogle(
        val context: Context) : AuthEvents()
    data class StartWithMicrosoft(
        val activity: MainActivity
    ): AuthEvents()
    data class HandleAuth(
         val intent: Intent
    ) : AuthEvents()
}