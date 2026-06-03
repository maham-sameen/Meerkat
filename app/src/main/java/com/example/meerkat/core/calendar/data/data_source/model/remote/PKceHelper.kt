package com.example.meerkat.core.calendar.data.data_source.model.remote

import android.util.Base64
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import dagger.hilt.android.scopes.ViewModelScoped
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PkceHelper @Inject constructor(){

    fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)
        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        return Base64.encodeToString(bytes, encoding)
    }

    fun generateCodeChallenge(codeVerifier: String): String {
        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val digest = MessageDigest.getInstance(ConstantCalendar.MESSAGE_DIGEST_ALGORITHM)
        val hash = digest.digest(codeVerifier.toByteArray())
        return Base64.encodeToString(hash, encoding)
    }
}
