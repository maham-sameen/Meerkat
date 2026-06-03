package com.example.meerkat.keycloak.auth.util


import com.auth0.android.jwt.JWT
import net.openid.appauth.AuthState

class AuthStateToAuthResultMapper : Mapper<AuthState, AuthResult> {
    override fun map(from: AuthState): AuthResult {
        val idToken = from.idToken
        return if (idToken != null) {
            val jwt = JWT(idToken)
            AuthResult(
                data = User(
                    id = jwt.getClaim("sub").asString().orEmpty(),
                    name = jwt.getClaim("name").asString().orEmpty(),
                    image = jwt.getClaim("picture").asString().orEmpty(),
                    email = jwt.getClaim("email").asString().orEmpty()
                ),
                errorMessage = null
            )
        } else {
            AuthResult(
                data = null,
                errorMessage = ""
            )
        }
    }
}