package com.example.meerkat.feature_auth.domain.use_case

import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.UserRepo

class GetUsers(
    private val repository: UserRepo
) {
    suspend  operator fun invoke(): List<User>{
        println("\uD83E\uDE9D [GetUsers] ")
        return repository.getAllUsers()
    }
}

