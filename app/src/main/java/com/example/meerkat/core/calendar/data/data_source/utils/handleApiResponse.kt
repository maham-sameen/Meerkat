package com.example.meerkat.core.calendar.data.data_source.utils

import com.example.meerkat.core.calendar.data.data_source.local.AuthLocalDataSource
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> handleApiResponse(
    dataSource : AuthLocalDataSource ,
    call: suspend (authHeader : String) -> Response<T>
): Result<T> {
    val authState = dataSource.getAuthState()
    val authService = dataSource.authorizationService
    return when (val tokenResult = authState.performActionWithFreshTokensSuspend(authService)){
        is TokenResult.Failure -> {
            println("\uD83C\uDFC9 [HandleApiResponse] token result is failure ${tokenResult.exception.toString()}")
            val ex = tokenResult.exception
            return handleTokenFailure(ex , onSaveAuthState = {
                dataSource.saveAuthState(authState)
            })
        }
        is TokenResult.Success -> {
            println("\uD83C\uDFC9 [HandleApiResponse] token result success ${tokenResult.toString()}")
            val accessToken = tokenResult.accessToken
            val authHeader = "Bearer $accessToken"
            try {
                val response = call(authHeader)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        println("\uD83C\uDFC9 [HandleApiResponse] body is not null")
                        Result.success(body)
                    } else {
                        println("\uD83C\uDFC9 [HandleApiResponse] body is null ${body.toString()}")
                        Result.failure(NullPointerException("Response body is null"))
                    }
                } else {
                    println("\uD83C\uDFC9 [HandleApiResponse] APi error")
                    Result.failure(Exception("API Error: ${response.code()} - ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                println("\uD83C\uDFC9 [HandleApiResponse] catch block here is ${e.message}")
                if (e is CancellationException) throw e
                Result.failure(e)
            }
        }
    }
}
