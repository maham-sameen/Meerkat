package com.example.meerkat.feature_auth.data.data_source.remote

import android.content.Intent
import androidx.core.net.toUri
import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import com.example.meerkat.core.calendar.data.data_source.local.AuthLocalDataSource
import com.example.meerkat.core.calendar.data.data_source.mapper.AuthStateToAuthResultMapper
import com.example.meerkat.core.calendar.data.data_source.model.remote.PkceHelper
import com.example.meerkat.feature_auth.domain.model.AuthResult
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

class AuthRemoteClientUtils(
    private val emailClientUtils: EmailClientUtils,
    private val authLocalDataSource: AuthLocalDataSource,
    private val pkceHelper: PkceHelper ,
){
    private val mapper: AuthStateToAuthResultMapper = AuthStateToAuthResultMapper()



    // https://medium.com/androiddevelopers/authenticating-on-android-with-the-appauth-library-7bea226555d5
    fun attemptAuthorization(
        launch : (Intent) -> Unit
    ) {
        try{
            Timber.tag(Constants.appName).i("🔐[AuthRemoteClientUtils] Starting authorization attempt...")
            val verifier = pkceHelper.generateCodeVerifier()
            val challenge = pkceHelper.generateCodeChallenge(verifier)

            val builder = AuthorizationRequest.Builder(
                authLocalDataSource.authServiceConfig,
                ConstantCalendar.CLIENT_ID,
                ResponseTypeValues.CODE,
                ConstantCalendar.URL_AUTH_REDIRECT.toUri()
            )
                .setCodeVerifier(
                    verifier,
                    challenge,
                    ConstantCalendar.CODE_VERIFIER_CHALLENGE_METHOD
                )

            builder.setScopes(
                ConstantCalendar.SCOPE_PROFILE,
                ConstantCalendar.SCOPE_EMAIL,
                ConstantCalendar.SCOPE_OPENID,
                ConstantCalendar.SCOPE_CALENDAR
            )

            Timber.tag(Constants.appName).i("📦[AuthRemoteClientUtils]  AuthorizationRequest built with scopes.")

            val request = builder.build()
            // ActivityNotFoundException
            val authIntent =
                authLocalDataSource.authorizationService.getAuthorizationRequestIntent(request)
            Timber.tag(Constants.appName).i("🚀[AuthRemoteClientUtils] Launching authorization intent...")
            launch(authIntent)
        }catch ( e : Exception){
            if(e is CancellationException) throw  e
            e.printStackTrace()
        }
    }


    suspend fun handleAuthorizationResponse(intent: Intent): AuthResult {
        try{
            Timber.tag(Constants.appName).i("📨 [AuthRemoteClientUtils] handleAuthorizationResponse() called.")

            val authorizationResponse = AuthorizationResponse.fromIntent(intent)
            val error = AuthorizationException.fromIntent(intent)

            var authState = AuthState(authorizationResponse, error)

            Timber.tag(Constants.appName).i(" [AuthRemoteClientUtils] after authorizationResponse , authState : ${authState.jsonSerializeString()}")

            if (authorizationResponse != null) {
                Timber.tag(Constants.appName).i("✅[AuthRemoteClientUtils] Received AuthorizationResponse.")

                val tokenExchangeRequest = authorizationResponse.createTokenExchangeRequest()
                Timber.tag(Constants.appName).i("🔁 [AuthRemoteClientUtils] Exchanging code for tokens...")

                val response = performTokenRequestSuspend(
                    authLocalDataSource.authorizationService,
                    tokenExchangeRequest
                )

                if (response != null) {
                    Timber.tag(Constants.appName).i("✅ [AuthRemoteClientUtils] Token response received.")
                    authState.update(response, null)

                    // Save updated authState
                    authLocalDataSource.saveAuthState(authState)

                    Timber.tag(Constants.appName).i("✅[AuthRemoteClientUtils] Auth state saved.")
                    return mapper.map(authState)
                } else {
                    Timber.tag(Constants.appName).i("❌[AuthRemoteClientUtils] Token exchange failed.")
                    authState = AuthState()
                    authLocalDataSource.saveAuthState(authState)
                    return mapper.map(authState)
                }
            } else {
                Timber.tag(Constants.appName).i("❌[AuthRemoteClientUtils] Authorization response was null. Possibly user cancelled login.")
                authState = AuthState()
                authLocalDataSource.saveAuthState(authState)
                return mapper.map(authState)
            }
        }catch ( e : Exception){
            Timber.tag(Constants.appName).i("❌ [AuthRemoteClientUtils] Exception during handleAuthorizationResponse: ${e.message}")
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
            Timber.tag(Constants.appName).i("%snull", "\uD83C\uDFA1 [AuthRemoteClientUtils] performTokenRequestSuspend() , \n " +
                    " authorizationService ${authorizationService.toString()} , \n ")
            if (exception != null) {
                Timber.tag(Constants.appName).i("[AuthRemoteClientUtils] exception is not null ${exception.toString()}")
                cont.resume(null)
            } else {
                Timber.tag(Constants.appName).i("[AuthRemoteClientUtils] we got response : ${response.toString()}")
                cont.resume(response)
            }
        }
    }


    suspend fun signInWithEmail(
        email : String ,
        password : String ,
    ): Result<String>{
        Timber.tag(Constants.appName).i("\uD83D\uDEFA [AuthRemoteClientUtils] signInWithEmail , email : $email , password : $password")
        return emailClientUtils.signInWithEmail(
            email = email,
            password = password
        )
    }

    suspend fun signUpEmail(
        email : String ,
        password : String
    ): AuthResult {
        Timber.tag(Constants.appName).i("\uD83D\uDEFA [AuthRemoteClientUtils] signUpWithEmail , email : $email , password : $password")
        return emailClientUtils.signUpWithEmail(
            email = email,
            password = password
        )
    }
}
/**
 *
 * What is FirebaseUser in Firebase Authentication?
 * FirebaseUser is an object that represents a user who has signed in using Firebase Authentication. This
 * object gives you access to information about the authenticated user, such as their unique ID, email
 * address, display name, etc.
 *
 * What is an ID Token?
 * An ID Token is a short-lived token (JWT - JSON Web Token) that is used to prove the identity of the
 * authenticated user. It's generated when the user signs in to your app using Firebase Authentication,
 * and you can send this token to your backend server to validate the user's identity.
 *
 * What does getIdToken() do?
 * The getIdToken() method on FirebaseUser retrieves the Firebase Authentication ID Token for the currently
 * signed-in user. This token is used to authenticate requests on your backend server.
 *
 * When you call getIdToken(), Firebase will generate and return this token asynchronously via a Task. You
 * can use this token to verify the user’s identity on your server.
 *
 * Parameters of getIdToken():
 * forceRefresh (Boolean):
 * This parameter is optional. By default, Firebase will attempt to get the ID token from the cache (it
 * will use a previously issued token if it's still valid).
 * If you set forceRefresh = true, it will force Firebase to fetch a fresh ID token from the Firebase server,
 * ignoring any cached token. This is useful if you suspect the token is no longer valid (e.g., the user has
 * changed their email, password, or other credentials).
 *
 * Exception:
 * FirebaseAuthInvalidUserException:
 * This exception can be thrown if you try to call getIdToken() and the user’s account has been disabled,
 * deleted, or their credentials are no longer valid. It will also be thrown if you use forceRefresh = true
 * and the user is no longer valid (e.g., the user has logged out or their account is inactive).
 *
 * Returns:
 * Task<GetTokenResult>:
 * This method returns a Task that will contain the GetTokenResult. The GetTokenResult object contains the
 * actual ID token string that you can send to your backend server for verification.
 * Task in Firebase is a way of handling asynchronous operations. You can use methods like
 * addOnCompleteListener() or addOnSuccessListener() to handle the result once the task is complete.
 *
 * Why do we need the ID Token?
 * The ID Token is important when you're working with Firebase Authentication, especially when you need to
 * communicate with your own backend. You can send this token to your backend server to prove that the user
 * is authenticated. On the server side, you can use Firebase Admin SDK or Firebase's server-side libraries
 * to validate the ID token to ensure it has not been tampered with and that the user is still authenticated.
 *
 *
 *
 *
 * What is GetTokenResult?
 * GetTokenResult is an object in Firebase Authentication that contains the ID Token and some metadata
 * about the token, such as the sign-in provider used to authenticate the user.
 *
 * What is the "sign-in provider"?
 * The "sign-in provider" refers to the method through which the user signed in to your app, such as:
 *
 * anonymous (anonymous authentication)
 * custom (custom token authentication)
 * phone (phone number authentication)
 * password (email/password authentication)
 * Key Point:
 * The sign-in provider is not the same as the provider ID. For example, anonymous and custom authentication
 * methods are not technically considered "providers" in Firebase’s terminology, but they still appear as a
 * string in the ID token's metadata.
 * This is important because Firebase uses this metadata to identify which method was used for authentication,
 * and you can use this information when managing the user’s session or handling specific logic based on
 * their sign-in method.
 *
 */