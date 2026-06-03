package com.example.meerkat.feature_auth.domain.use_case

import com.example.meerkat.feature_auth.domain.use_case.auth.AuthGoogleClient
import com.example.meerkat.feature_auth.domain.use_case.auth.AuthMicrosoftClient
import com.example.meerkat.feature_auth.domain.use_case.auth.HandleAuthorization
import com.example.meerkat.feature_auth.domain.use_case.auth.IsValidEmailAddress
import com.example.meerkat.feature_auth.domain.use_case.auth.SignInEmail
import com.example.meerkat.feature_auth.domain.use_case.auth.SignUpEmail

data class AuthUseCases(
    val authGoogleClient: AuthGoogleClient,
    val handleAuthorization: HandleAuthorization,
    val authMicrosoftClient: AuthMicrosoftClient,
    val signInEmail: SignInEmail,
    val checkPasswordStrength: CheckPasswordStrength,
    val signUpEmail: SignUpEmail,
    val getUsers: GetUsers,
    val getUser: GetUser,
    val isValidEmailAddress: IsValidEmailAddress
)
