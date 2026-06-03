package com.example.meerkat.feature_auth.domain.use_case.auth

import android.util.Patterns

class IsValidEmailAddress {
    operator fun invoke(email : String) : Boolean{
        return email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}