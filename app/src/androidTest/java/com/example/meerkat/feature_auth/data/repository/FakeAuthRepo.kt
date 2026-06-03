package com.example.meerkat.feature_auth.data.repository

import android.content.Context
import android.content.Intent
import com.example.meerkat.MainActivity
import com.example.meerkat.feature_auth.data.data_source.remote.EmailClientUtils
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.repository.AuthRepo
import com.google.firebase.auth.FirebaseAuth

class FakeAuthRepo(
    private val emailClientUtils: EmailClientUtils
) : AuthRepo {
    override suspend fun authWithGoogle(
        context: Context,
        onLaunch: (Intent) -> Unit
    ) {

    }

    override suspend fun authWithMicrosoft(activity: MainActivity): AuthResult {
        return AuthResult(
            errorMessage = "Unknown Error" ,
            data = null
        )
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<String> {
        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)
        return emailClientUtils.signInWithEmail(
            email = email,
            password = password
        )
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): AuthResult {
        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)
       return emailClientUtils.signUpWithEmail(
           email = email,
           password = password
       )
    }

    override suspend fun handleAuthorizationResponse(intent: Intent): AuthResult {
        return AuthResult(
            errorMessage = "Unknown Error" ,
            data = null
        )
    }
}