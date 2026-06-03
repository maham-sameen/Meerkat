package com.example.meerkat.feature_auth.domain.repository

import com.example.meerkat.feature_auth.domain.model.User

interface UserRepo {
    suspend fun getAllUsers(): List<User>
    suspend fun getUser(id: String): User?
    suspend fun deleteUser(id: String): Boolean
    suspend fun saveUser(user: User) : Result<String>

    suspend fun getUserViaEmail(email : String) : User?
}