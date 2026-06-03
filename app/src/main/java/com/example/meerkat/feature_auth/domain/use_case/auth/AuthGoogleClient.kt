package com.example.meerkat.feature_auth.domain.use_case.auth

import android.content.Context
import android.content.Intent
import com.example.meerkat.Constants
import com.example.meerkat.feature_auth.domain.repository.AuthRepo
import timber.log.Timber

class AuthGoogleClient (
    private val authRepo: AuthRepo,
){
    suspend operator fun invoke(
        context: Context,
        onLaunch : (Intent) -> Unit ,
    ){
        Timber.tag(Constants.appName).i("we are going to do authentication with google")
        authRepo.authWithGoogle(
            context = context,
            onLaunch = onLaunch
        )
    }
}