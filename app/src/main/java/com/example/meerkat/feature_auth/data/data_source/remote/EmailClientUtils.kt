package com.example.meerkat.feature_auth.data.data_source.remote

import com.example.meerkat.Constants
import com.example.meerkat.feature_auth.data.data_source.mapper.FirebaseUserToAuthResultMapper
import com.example.meerkat.feature_auth.data.data_source.mapper.setFailedAuthResult
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailClientUtils
@Inject
constructor() {

    private val mapper  : FirebaseUserToAuthResultMapper = FirebaseUserToAuthResultMapper()
    private val auth = Firebase.auth


    /**
     * Sign in an existing user with email and password
     */
    suspend fun signInWithEmail(
        email: String,
        password: String,
    ) : Result<String> {
       return try {
           auth.signInWithEmailAndPassword(email, password).await()
           Result.success("Sign in Successfully")
       }catch (e : Exception){
           if(e is CancellationException) throw e
           with(NonCancellable){
               Result.failure(e)
           }
       }
    }

    /**
     * Sign up a new user with email and password
     */
    suspend fun signUpWithEmail(email: String, password: String): AuthResult {
        return try {
            Timber.tag(Constants.appName).i("\uD83D\uDE00  \uD83D\uDE00 \uD83D\uDE00 SIGN UP WITH EMAIL")
            val result =  auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return setFailedAuthResult("Error creating account. Please try again.")
            Timber.tag(Constants.appName).i("\uD83D\uDC79 email token : ${user.getIdToken(true).await().token.toString()} , email uid ${user.uid}")
            return  mapper.map(user)
        }catch (e: Exception) {
            if(e is FirebaseAuthUserCollisionException){
                setFailedAuthResult("This email is already in use. Please sign in instead.")
            }
            if(e is CancellationException) throw e
            with(NonCancellable){
                setFailedAuthResult(
                    e.localizedMessage ?: "Unknown error occurred."
                )
            }
        }
    }
}

/**
 *  auth.signInWithEmailAndPassword(email, password) // ✅ Correct method for sign-in
 *             .addOnCompleteListener { task ->
 *                 if (task.isSuccessful) {
 *                     onResult("Sign in successful") // ✅ Correct success message
 *                 } else {
 *                     val exception = task.exception
 *                     when (exception) {
 *                         is FirebaseAuthInvalidUserException -> {
 *                             onResult("No account exists with this email.")
 *                         }
 *                         is FirebaseAuthInvalidCredentialsException -> {
 *                             onResult("Incorrect password or email. Please try again.")
 *                         }
 *                         else -> {
 *                             onResult("Sign in failed: ${exception?.localizedMessage ?: "Unknown error"}")
 *                         }
 *                     }
 *                 }
 *             }
 */