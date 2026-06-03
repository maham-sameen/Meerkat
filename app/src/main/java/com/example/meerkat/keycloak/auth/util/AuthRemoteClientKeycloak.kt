package com.example.meerkat.keycloak.auth.util
import android.content.Intent
import androidx.core.net.toUri
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import timber.log.Timber
import kotlin.coroutines.resume

class AuthRemoteClientUtilsKeycloak(
    private val authLocalDataSource: AuthLocalDataSourceKeycloak,
    private val pkceHelperKeycloak: PkceHelperKeycloak,
){
    private val mapper: AuthStateToAuthResultMapper = AuthStateToAuthResultMapper()



    // https://medium.com/androiddevelopers/authenticating-on-android-with-the-appauth-library-7bea226555d5
    fun attemptAuthorization(
        launch : (Intent) -> Unit
    ) {
        try{
            Timber.tag(ConstantsKeycloak.appName).i("🔐[AuthRemoteClientUtils] Starting authorization attempt...")
            val verifier = pkceHelperKeycloak.generateCodeVerifier()
            val challenge = pkceHelperKeycloak.generateCodeChallenge(verifier)

            val builder = AuthorizationRequest.Builder(
                authLocalDataSource.authServiceConfig
                ,
                ConstantsKeycloak.CLIENT_ID,
                ResponseTypeValues.CODE,
                ConstantsKeycloak.URL_AUTH_REDIRECT.toUri(),
            )
                .setCodeVerifier(
                    verifier,
                    challenge,
                    ConstantsKeycloak.CODE_VERIFIER_CHALLENGE_METHOD
                )


            builder.setScopes(
                ConstantsKeycloak.SCOPE_PROFILE,
                ConstantsKeycloak.SCOPE_EMAIL,
                ConstantsKeycloak.SCOPE_OPENID,
            )
//                .setPrompt(AuthorizationRequest.Prompt.LOGIN)
//                .setUiLocales("uk")

            Timber.tag(ConstantsKeycloak.appName).i("📦[AuthRemoteClientUtils]  AuthorizationRequest built with scopes.")

            val request = builder.build()
            // ActivityNotFoundException
            val authIntent =
                authLocalDataSource.authorizationService.getAuthorizationRequestIntent(request)
            Timber.tag(ConstantsKeycloak.appName).i("🚀[AuthRemoteClientUtils] Launching authorization intent...")
            launch(authIntent)
        }catch ( e : Exception){
            if(e is CancellationException) throw  e
            e.printStackTrace()
        }
    }


    suspend fun handleAuthorizationResponse(intent: Intent): AuthResult {
        try{
            Timber.tag(ConstantsKeycloak.appName).i("📨 [AuthRemoteClientUtils] handleAuthorizationResponse() called.")

            val authorizationResponse = AuthorizationResponse.fromIntent(intent)
            val error = AuthorizationException.fromIntent(intent)

            var authState = AuthState(authorizationResponse, error)

            Timber.tag(ConstantsKeycloak.appName).i(" [AuthRemoteClientUtils] after authorizationResponse , authState : ${authState.jsonSerializeString()}")

            if (authorizationResponse != null) {
                Timber.tag(ConstantsKeycloak.appName).i("✅[AuthRemoteClientUtils] Received AuthorizationResponse.")

                val tokenExchangeRequest = authorizationResponse.createTokenExchangeRequest()
                Timber.tag(ConstantsKeycloak.appName).i("🔁 [AuthRemoteClientUtils] Exchanging code for tokens...")

                val response = performTokenRequestSuspend(
                    authLocalDataSource.authorizationService,
                    tokenExchangeRequest
                )

                if (response != null) {
                    Timber.tag(ConstantsKeycloak.appName).i("✅ [AuthRemoteClientUtils] Token response received.")
                    authState.update(response, null)

                    // Save updated authState
                    authLocalDataSource.saveAuthState(authState)

                    Timber.tag(ConstantsKeycloak.appName).i("✅[AuthRemoteClientUtils] Auth state saved.")
                    return mapper.map(authState)
                } else {
                    Timber.tag(ConstantsKeycloak.appName).i("❌[AuthRemoteClientUtils] Token exchange failed.")
                    authState = AuthState()
                    authLocalDataSource.saveAuthState(authState)
                    return mapper.map(authState)
                }
            } else {
                Timber.tag(ConstantsKeycloak.appName).i("❌[AuthRemoteClientUtils] Authorization response was null. Possibly user cancelled login.")
                authState = AuthState()
                authLocalDataSource.saveAuthState(authState)
                return mapper.map(authState)
            }
        }catch ( e : Exception){
            Timber.tag(ConstantsKeycloak.appName).i("❌ [AuthRemoteClientUtils] Exception during handleAuthorizationResponse: ${e.message}")
            if(e is CancellationException) throw e
            e.printStackTrace()
            with(NonCancellable){
                // Optional: reset auth state on error
                val authState = AuthState()
                authLocalDataSource.saveAuthState(authState)
                return mapper.map(authState)
            }
        }
    }

    suspend fun performTokenRequestSuspend(
        authorizationService: AuthorizationService,
        request: TokenRequest
    ): TokenResponse? = suspendCancellableCoroutine { cont ->
        authorizationService.performTokenRequest(request) { response, exception ->
            Timber.tag(ConstantsKeycloak.appName).i("%snull", "\uD83C\uDFA1 [AuthRemoteClientUtils] performTokenRequestSuspend() , \n " +
                    " authorizationService ${authorizationService.toString()} , \n ")
            if (exception != null) {
                Timber.tag(ConstantsKeycloak.appName).i("[AuthRemoteClientUtils] exception is not null ${exception.toString()}")
                cont.resume(null)
            } else {
                Timber.tag(ConstantsKeycloak.appName).i("[AuthRemoteClientUtils] we got response : ${response.toString()}")
                cont.resume(response)
            }
        }
    }

}
