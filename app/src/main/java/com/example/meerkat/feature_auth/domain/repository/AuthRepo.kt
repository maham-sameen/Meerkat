package com.example.meerkat.feature_auth.domain.repository

import android.content.Context
import android.content.Intent
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.MainActivity

interface AuthRepo {
   suspend fun authWithGoogle(
        context: Context,
        onLaunch : (Intent) -> Unit
    )

   suspend fun authWithMicrosoft(
       activity: MainActivity
   ): AuthResult

    suspend fun signInWithEmail(email : String, password : String) : Result<String>
    suspend fun signUpWithEmail(email : String, password : String) : AuthResult

    suspend fun handleAuthorizationResponse(intent: Intent): AuthResult
}