package com.example.meerkat.feature_auth.data.repository

import com.example.meerkat.feature_auth.data.data_source.remote.UserRemoteDataSource
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FakeUserRepo(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepo {

    override suspend fun getAllUsers(): List<User> {
        Firebase.firestore.useEmulator("10.0.2.2", 8080)
        return userRemoteDataSource.getAllUsers()
    }

    override suspend fun getUser(id: String): User? {
        Firebase.firestore.useEmulator("10.0.2.2", 8080)
       return userRemoteDataSource.getUser(id)
    }

    override suspend fun deleteUser(id: String): Boolean {
        Firebase.firestore.useEmulator("10.0.2.2", 8080)
        return userRemoteDataSource.deleteUser(id)
    }

    override suspend fun saveUser(user: User): Result<String> {
        Firebase.firestore.useEmulator("10.0.2.2", 8080)
        return userRemoteDataSource.registerUser(user)
    }

    override suspend fun getUserViaEmail(email: String): User? {
        Firebase.firestore.useEmulator("10.0.2.2", 8080)
        return userRemoteDataSource.getUserByEmail(email)
    }
}