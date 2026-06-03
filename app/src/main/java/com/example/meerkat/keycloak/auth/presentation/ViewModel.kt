package com.example.meerkat.keycloak.auth.presentation

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meerkat.keycloak.auth.util.AuthRemoteClientUtilsKeycloak
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel
    @Inject constructor(
        private val authRemoteClientUtils: AuthRemoteClientUtilsKeycloak
    ): ViewModel() {


    fun goWithKeyCloak(
        noAccount : (Intent) -> Unit
    ){
        return authRemoteClientUtils.attemptAuthorization {
            noAccount(it)
        }
    }

    fun handleAuth(intent: Intent){
        viewModelScope.launch {
            authRemoteClientUtils.handleAuthorizationResponse(intent)
        }
    }
}