package com.example.meerkat.feature_auth.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.mapper.Mapper
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseUserToAuthResultMapper : Mapper<FirebaseUser, AuthResult> {
    override fun map(from: FirebaseUser): AuthResult {

        return AuthResult(
            data = User(
                id = from.uid,
                name = from.displayName.orEmpty(),
                image = from.photoUrl?.toString().orEmpty(),
                email = from.email.orEmpty()
            ),
            errorMessage = null
        )
    }
}