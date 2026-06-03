package com.example.meerkat.keycloak.auth.util


import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

class PkceHelperKeycloak (){
    fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)
        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        return Base64.encodeToString(bytes, encoding)
    }

    fun generateCodeChallenge(codeVerifier: String): String {
        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val digest = MessageDigest.getInstance(ConstantsKeycloak.MESSAGE_DIGEST_ALGORITHM)
        val hash = digest.digest(codeVerifier.toByteArray())
        return Base64.encodeToString(hash, encoding)
    }
}
