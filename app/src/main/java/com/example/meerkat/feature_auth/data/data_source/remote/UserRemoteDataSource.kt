package com.example.meerkat.feature_auth.data.data_source.remote

import android.util.Log
import com.example.meerkat.Constants
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.presentation.util.AuthConstant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(){
    private val mFireStore = FirebaseFirestore.getInstance()
    private val TAG : String = "User Database"


    suspend fun registerUser(
        userInfo: User,
    )  : Result<String>{
        Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] registerUser() , userInfo : ${userInfo.toString()}")
        return try{
            mFireStore.collection(AuthConstant.USERS)
                .document(userInfo.id)
                .set(userInfo, SetOptions.merge()).await()
            Result.success("Successfully registered user")
        }catch (e : Exception){
            Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] registerUser()  , e : ${e.toString()}")
            if(e is CancellationException) throw e
            with(NonCancellable) {
                FirebaseAuth.getInstance().signOut()
                Result.failure(e)
            }
        }
    }

    suspend fun getAllUsers() : List<User> {
        return try{
            Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] getAllUsers() , ")
            val result = mFireStore.collection(AuthConstant.USERS).get().await()
            val userList = result.documents.mapNotNull { it.toObject(User::class.java) }
            Timber.tag(Constants.appName).i("Fetched ${userList.size} users")
            Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] getAllUsers() , userList : ${userList.toString()} ")
            userList
        }
        catch (e : Exception){
            Timber.tag(Constants.appName).i("Error fetching users: ${e.message}")
            Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] getAllUsers() , exception : ${e.toString()} ")
            if(e is CancellationException) throw e
            with(NonCancellable) {
                listOf()
            }
        }
    }

    suspend fun getUser(userID : String) : User?{
        Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] getUser , userId : $userID")
        if(userID.isEmpty()){
            return null
        }
        return try{
            val document = mFireStore.collection(AuthConstant.USERS)
                .document(userID)
                .get().await()
            document.toObject(User::class.java)
        }catch (e : Exception){
            if(e is CancellationException) throw e
            with(NonCancellable) {
                null
            }
        }
    }

    suspend fun deleteUser(
        documentID : String ,
        //  onResult: (Boolean) -> Unit
    ) : Boolean{
        Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] deleteUser : ${documentID.toString()}")
        return try {
            mFireStore.collection(AuthConstant.USERS)
                .document(documentID)
                .delete().await()
            Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] deleteUser : returning true")
            true
        }catch (e : Exception){
            Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] deleteUser : exception : ${e.toString()}")
            if(e is CancellationException) throw e
            with(NonCancellable){
                false
            }
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] getUserByEmail , email : $email")
        if (email.isEmpty()) {
            return null
        }
        return try {
            // Query Firestore to get the user document with the matching email
            val result = mFireStore.collection(AuthConstant.USERS)
                .whereEqualTo("email", email) // assuming email field in Firestore is "email"
                .get()
                .await()

            Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] getUserByEmail , result : ${result.toString()}")

            // Check if there is a document returned
            if (result.isEmpty) {
                null
            } else {
                result.documents.firstOrNull()?.toObject(User::class.java)
            }
        } catch (e: Exception) {
            Timber.tag(Constants.appName).i("Error fetching user by email: ${e.message}")
            Timber.tag(Constants.appName).i("\uD83C\uDFDF [UserRemoteDataSource] getUserByEmail , e : ${e.toString()}")
            if (e is CancellationException) throw e
            with(NonCancellable) {
                null
            }
        }
    }
}