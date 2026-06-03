package com.example.meerkat.feature_auth.data.repository

import com.example.meerkat.Constants
import com.example.meerkat.feature_auth.data.data_source.remote.UserRemoteDataSource
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import timber.log.Timber

class UserRepoImp(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepo {
    override suspend fun getAllUsers(): List<User> {
        Timber.tag(Constants.appName).i("\uD83E\uDE9D [UserRepoImp] getAllUsers()")
        return remoteDataSource.getAllUsers()
    }

    override suspend fun getUser(id: String): User? {
        Timber.tag(Constants.appName).i("\uD83E\uDE9D [UserRepoImp] getUser : id : $id")
        return remoteDataSource.getUser(userID = id)
    }

    override suspend fun deleteUser(id: String
    ) : Boolean{
        Timber.tag(Constants.appName).i("\uD83E\uDE9D [UserRepoImp] deleteUser : id : $id")
        return remoteDataSource.deleteUser(
            documentID = id,
        )
    }

    override suspend fun saveUser(
        user: User
    ) : Result<String> {
        Timber.tag(Constants.appName).i("\uD83E\uDE9D [UserRepoImp] saveUser : user : ${user.toString()}")
        return remoteDataSource.registerUser(
            userInfo = user
        )
    }

    override suspend fun getUserViaEmail(email: String): User? {
        Timber.tag(Constants.appName).i("\uD83E\uDE9D [UserRepoImp] getUserViaEmail , email : $email")
        return remoteDataSource.getUserByEmail(email)
    }
}