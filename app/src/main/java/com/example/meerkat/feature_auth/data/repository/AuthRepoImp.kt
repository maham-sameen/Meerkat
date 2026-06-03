package com.example.meerkat.feature_auth.data.repository

import android.content.Context
import android.content.Intent
import com.example.meerkat.feature_auth.data.data_source.remote.AuthRemoteClientUtils
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.repository.AuthRepo
import com.example.meerkat.MainActivity

class AuthRepoImp (
    private val remoteDataSource: AuthRemoteClientUtils,
): AuthRepo {

    override suspend fun authWithGoogle(
        context: Context,
        onLaunch : (Intent) -> Unit
    ){
       return remoteDataSource.attemptAuthorization(onLaunch)
    }

    override suspend fun authWithMicrosoft(activity: MainActivity
    ): AuthResult {
        return AuthResult(null , null)
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String,
    ) : Result<String>{
       return remoteDataSource.signInWithEmail(
            email = email,
            password = password
        )
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): AuthResult {
        return  remoteDataSource.signUpEmail(
            email = email,
            password = password
        )
    }

    override suspend fun handleAuthorizationResponse(intent: Intent): AuthResult {
        return remoteDataSource.handleAuthorizationResponse(intent)
    }
}