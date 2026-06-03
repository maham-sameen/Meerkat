package com.example.meerkat.core.calendar.data.data_source.utils

import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService


sealed class TokenResult {
    data class Success(val accessToken: String?, val idToken: String?) : TokenResult()
    data class Failure(val exception: Exception) : TokenResult()
}
 suspend fun AuthState.performActionWithFreshTokensSuspend(
    service: AuthorizationService
): TokenResult {
    return suspendCancellableCoroutine { cont ->
        println("\uD83C\uDFC9 [PerformActionWithFreshTokenSuspend] performActionWithFreshTokenSuspended ${cont.toString()}")
        this.performActionWithFreshTokens(service) { accessToken, idToken, ex ->
            if (ex != null) {
                println("\uD83C\uDFC9 [PerformActionWithFreshTokenSuspend] ex not null ${ex.toString()}")
                cont.resume(
                    TokenResult.Failure(ex)
                ) { cause, _, _ -> null
                    (cause) }
            } else {
                println(" \uD83C\uDFC9 [PerformActionWithFreshTokenSuspend] accessToken : ${accessToken.toString()} , idToken : ${idToken.toString()}")
                cont.resume(
                    TokenResult.Success(accessToken, idToken)
                ) { cause, _, _ -> null
                    (cause) }
            }
        }
    }
}