package com.example.meerkat.feature_auth.domain.use_case.auth


import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.MainActivity

class AuthMicrosoftClient(
    private val makeUniqueAccount: MakeUniqueAccount
) {
    suspend operator fun invoke(
        activity: MainActivity,
        users : List<User>?,
        onRegister : (String) -> Unit
    ) : AuthResult{
        return AuthResult(null , null)
    }
}