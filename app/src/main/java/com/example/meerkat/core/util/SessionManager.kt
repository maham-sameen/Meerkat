package com.example.meerkat.core.util

import com.example.meerkat.core.calendar.data.data_source.local.AuthLocalDataSource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay

class SessionManager(
    private val authLocalDataSource: AuthLocalDataSource,
) {
    private val firebaseAuth: FirebaseAuth = Firebase.auth

    suspend fun isUserLoggedIn(): Boolean {
            val user = firebaseAuth.currentUser
            if (user != null) return true

        // ✅ Check OAuth2 session
        val authState = authLocalDataSource.getAuthState()
        return if (authState.needsTokenRefresh) {
            val refreshed = authLocalDataSource.refreshToken()
            refreshed?.isAuthorized == true
        } else {
            authState.isAuthorized
        }
    }
}
