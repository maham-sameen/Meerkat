package com.example.meerkat.feature_auth.domain.use_case.auth

import android.content.Intent
import com.example.meerkat.Constants
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.AuthRepo
import timber.log.Timber

class HandleAuthorization (
    private val authRepo: AuthRepo,
    private val makeUniqueAccount: MakeUniqueAccount
){

    suspend operator fun invoke(
        intent : Intent ,
        users : List<User>?,
    ) : AuthResult{
       val authResult =  authRepo.handleAuthorizationResponse(intent)
        Timber.tag(Constants.appName).i("handle authorization for google : authResult : ${authResult.toString()}")
       return makeUniqueAccount
           .invoke(
               users = users,
               authResult = authResult
           )
    }
}