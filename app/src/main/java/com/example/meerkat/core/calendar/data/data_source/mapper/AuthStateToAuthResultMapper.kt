package com.example.meerkat.core.calendar.data.data_source.mapper

import com.auth0.android.jwt.JWT
import com.example.meerkat.feature_auth.data.data_source.mapper.setFailedAuthResult
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.model.User
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
            setFailedAuthResult("Something Missing")
        }
    }
}