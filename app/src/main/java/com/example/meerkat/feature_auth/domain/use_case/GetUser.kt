package com.example.meerkat.feature_auth.domain.use_case

import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.UserRepo


class GetUser(
    private val repository: UserRepo
){
    suspend  operator fun invoke(userID : String): User?{
        println("\uD83E\uDE9D [GetUser] , userId : $userID")
        return repository.getUser(userID)
    }
}