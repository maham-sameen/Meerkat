package com.example.meerkat.feature_auth.data.repository

import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.UserRepo

class FakeUserRepo : UserRepo {
    private val users = mutableListOf<User>()

    override suspend fun getAllUsers(): List<User> {
        return users.toList()
    }

    override suspend fun getUser(id: String): User? {
       return users.find { it.id == id }
    }

    override suspend fun deleteUser(id: String): Boolean {
        val user =  users.find { it.id==id }
        return if(user == null){
            false
        }else{
            users.remove(user)
        }
    }

    override suspend fun saveUser(user: User): Result<String> {
        users.add(user)
        return Result.success("User saved successfully")
    }

    override suspend fun getUserViaEmail(email: String): User? {
        return users.find { it.email == email }
    }
}