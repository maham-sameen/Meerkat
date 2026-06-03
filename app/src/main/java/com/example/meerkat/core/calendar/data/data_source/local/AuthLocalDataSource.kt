package com.example.meerkat.core.calendar.data.data_source.local

import android.content.Context
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import com.example.meerkat.core.calendar.data.data_source.utils.TokenResult
import com.example.meerkat.core.calendar.data.data_source.utils.performActionWithFreshTokensSuspend
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher

class AuthLocalDataSource
    (
    val context: Context
){
    private val Context.dataStore by preferencesDataStore(ConstantCalendar.AUTH_PREF)

    // ✅ Use strongly typed keys
    private val AUTH_STATE_KEY = stringPreferencesKey(ConstantCalendar.AUTH_STATE)
    private val CURRENT_USER_KEY = stringPreferencesKey(ConstantCalendar.CURRENT_USER_ID)


    val authServiceConfig: AuthorizationServiceConfiguration by lazy {
        AuthorizationServiceConfiguration(
            ConstantCalendar.URL_AUTHORIZATION.toUri(),
            ConstantCalendar.URL_TOKEN_EXCHANGE.toUri(),
            null,
            ConstantCalendar.URL_LOGOUT.toUri()
        )
    }

    val authorizationService: AuthorizationService by lazy {
        val appAuthConfiguration = AppAuthConfiguration.Builder()
            .setBrowserMatcher(
                BrowserAllowList(
                    VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                    VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB
                )
            ).build()

        AuthorizationService(context, appAuthConfiguration)
    }



    suspend fun saveAuthState(authState: AuthState){
        context.dataStore.edit {
            it[AUTH_STATE_KEY] = authState.jsonSerializeString()
        }
    }

    suspend fun getAuthState(): AuthState {
        val authStateString =  context.dataStore.data.map {
            it[AUTH_STATE_KEY]
        }.first()
       return if(authStateString != null) {
           AuthState.jsonDeserialize(authStateString)
       } else {
           AuthState()
       }
    }

    suspend fun refreshToken(): AuthState? {
        val authState = getAuthState()

        when (val result = authState.performActionWithFreshTokensSuspend(authorizationService)) {
            is TokenResult.Failure -> {
                // token refresh failed
                return null
            }
            is TokenResult.Success -> {
                // success - save updated state
                saveAuthState(authState)
                return authState
            }
        }
    }

    fun onCleared(){
        authorizationService.dispose()
    }
}