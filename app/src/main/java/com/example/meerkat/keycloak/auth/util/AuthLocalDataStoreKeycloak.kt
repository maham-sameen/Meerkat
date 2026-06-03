package com.example.meerkat.keycloak.auth.util



import android.content.Context
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher

class AuthLocalDataSourceKeycloak
    (
    val context: Context
){
    private val Context.dataStore by preferencesDataStore(ConstantsKeycloak.AUTH_PREF)

    // ✅ Use strongly typed keys
    private val AUTH_STATE_KEY = stringPreferencesKey(ConstantsKeycloak.AUTH_STATE)



    val authServiceConfig: AuthorizationServiceConfiguration by lazy {
        AuthorizationServiceConfiguration(
            ConstantsKeycloak.URL_AUTHORIZATION.toUri(),
            ConstantsKeycloak.URL_TOKEN_EXCHANGE.toUri(),
            ConstantsKeycloak.REGISTRATION_END_POINT.toUri(),
            ConstantsKeycloak.URL_LOGOUT.toUri() ,
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
/**
 *
 * ---
 *
 * # ✅ Option 1 (Best Practice): Run Keycloak with HTTPS in Docker
 *
 * Keycloak supports HTTPS directly.
 *
 * ## 🔥 Easiest Dev Setup (Self-Signed Certificate)
 *
 * ### 1️⃣ Generate a self-signed certificate
 *
 * On your Mac:
 *
 * ```bash
 * openssl req -x509 -newkey rsa:2048 \
 *   -keyout key.pem \
 *   -out cert.pem \
 *   -days 365 \
 *   -nodes
 * ```
 *
 * ---
 *
 * ### 2️⃣ Run Keycloak with HTTPS
 *
 * ```bash
 * docker run -p 8181:8443 \
 *   -e KC_HOSTNAME=10.0.2.2 \
 *   -e KC_HTTPS_CERTIFICATE_FILE=/opt/keycloak/conf/cert.pem \
 *   -e KC_HTTPS_CERTIFICATE_KEY_FILE=/opt/keycloak/conf/key.pem \
 *   -v $(pwd)/cert.pem:/opt/keycloak/conf/cert.pem \
 *   -v $(pwd)/key.pem:/opt/keycloak/conf/key.pem \
 *   quay.io/keycloak/keycloak:26 start-dev
 * ```
 *
 * Now Keycloak runs on:
 *
 * ```
 * https://10.0.2.2:8181
 * ```
 *
 * ---
 *
 * # ⚠️ BUT Emulator Will Reject Self-Signed Cert
 *
 * Android does NOT trust self-signed certificates by default.
 *
 * So you must:
 *
 * ---
 *
 * # ✅ Option 2 (Easier for Dev): Use mkcert (RECOMMENDED)
 *
 * This is MUCH cleaner.
 *
 * ### Install mkcert:
 *
 * ```bash
 * brew install mkcert
 * mkcert -install
 * ```
 *
 * Then:
 *
 * ```bash
 * mkcert localhost 10.0.2.2
 * ```
 *
 * It generates trusted certs for your machine.
 *
 * Now Android emulator will trust it automatically.
 *
 * ---
 *
 * # 🚀 Update Your Constants
 *
 * Change everything to:
 *
 * ```kotlin
 * const val ISSUER_URI =
 *     "https://10.0.2.2:8181/realms/fitness-oauth2"
 *
 * const val URL_AUTHORIZATION =
 *     "https://10.0.2.2:8181/realms/fitness-oauth2/protocol/openid-connect/auth"
 *
 * const val URL_TOKEN_EXCHANGE =
 *     "https://10.0.2.2:8181/realms/fitness-oauth2/protocol/openid-connect/token"
 *
 * const val URL_LOGOUT =
 *     "https://10.0.2.2:8181/realms/fitness-oauth2/protocol/openid-connect/logout"
 * ```
 *
 * ---
 *
 * # 💡 Even Better Option (Pro Dev Setup)
 *
 * Use:
 *
 * ## 👉 Caddy or Nginx as reverse proxy
 *
 * * Run Keycloak internally on HTTP
 * * Let proxy provide HTTPS
 * * Much cleaner architecture
 *
 * ---
 *
 * # 🎯 What I Recommend For You
 *
 * Since you're doing serious Android OAuth work:
 *
 * ✅ Use HTTPS in development
 * ✅ Use mkcert
 * ❌ Don’t disable AppAuth security long term
 *
 * ---
 *
 * # 🧠 Real World Flow
 *
 * | Environment | Protocol                     |
 * | ----------- | ---------------------------- |
 * | Local dev   | HTTPS (self-signed / mkcert) |
 * | Staging     | HTTPS (real cert)            |
 * | Production  | HTTPS (real cert)            |
 *
 * ---
 *
 * If you want, I can give you a **clean docker-compose.yml** with HTTPS working perfectly for emulator + Android.
 *
 * Just tell me 👌
 *
 */