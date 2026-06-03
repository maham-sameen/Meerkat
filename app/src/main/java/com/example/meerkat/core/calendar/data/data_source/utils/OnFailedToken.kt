package com.example.meerkat.core.calendar.data.data_source.utils

import com.example.meerkat.core.calendar.domain.model.SessionExpiredException
import net.openid.appauth.AuthorizationException

inline fun <reified T>  handleTokenFailure(ex: Exception , onSaveAuthState : () -> Unit): Result<T> {
    println("\uD83C\uDFC9 [HandleTokenFailure] token has expired : ${ex.toString()}")
    return if (ex is AuthorizationException &&
        (ex.error == "invalid_grant" || ex.code == 1007)
    ) {
        println("\uD83C\uDFC9 [HandleApiResponse] authorization exception")
        println("\uD83C\uDFC9 [HandleApiResponse] ex is invalid_grant")
        onSaveAuthState()
        Result.failure(SessionExpiredException())
    } else {
        Result.failure(ex)
    }
}
