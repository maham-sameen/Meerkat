package com.example.meerkat.keycloak.auth.util

class ConstantsKeycloak {


    companion object {

        const val  REGISTRATION_END_POINT = "https://192.168.1.5:8443/realms/myrealm/clients-registrations/openid-connect"
        val AUTH_PREF: String = "AUTH_PREF"
        const val SHARED_PREFERENCES_NAME = "AUTH_STATE_PREFERENCE"
        const val AUTH_STATE = "AUTH_STATE"

        const val SCOPE_OPENID = "openid"
        const val SCOPE_PROFILE = "profile"
        const val SCOPE_EMAIL = "email"

        const val CLIENT_ID = "android_client"

        // 👇 Replace YOUR_REALM
        const val ISSUER_URI = "https://192.168.1.5:8443/realms/myrealm"

        const val LOGOUT_REDIRECT_URI = "https://saadmirza.net/logout"
        val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
        val MESSAGE_DIGEST_ALGORITHM = "SHA-256"
        val URL_AUTH_REDIRECT = "https://saadmirza.net/redirect"
        val appName = "suspendapp"

        val URL_AUTHORIZATION = "https://192.168.1.5:8443/realms/myrealm/protocol/openid-connect/auth"
        val URL_TOKEN_EXCHANGE = "https://192.168.1.5:8443/realms/myrealm/protocol/openid-connect/token"
        val URL_LOGOUT = "https://192.168.1.5:8443/realms/myrealm/protocol/openid-connect/logout"

    }
}

