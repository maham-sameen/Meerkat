package com.example.meerkat.feature_auth.domain.use_case.auth


import com.example.meerkat.Constants
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class MakeUniqueAccount(
    private val repository: UserRepo
) {

    fun getDuplicatedUser(users: List<User>?, authResult: AuthResult): List<User>? {
        val currentEmail = authResult.data?.email ?: return null
        // Create a new list that includes the users plus the current authenticated user
        val updatedUsers = (users ?: emptyList()) + authResult.data
        Timber.tag(Constants.appName).i("[MakeUniqueAccount] getDuplicateUser : updatedUsers ${updatedUsers.toString()} , currentEmail : ${currentEmail.toString()}")
        return updatedUsers.filter { it.email == currentEmail }.takeIf { it.size > 1 }
    }


    suspend fun deleteDuplicatedUser(duplicateUsers: List<User>) = coroutineScope {
        duplicateUsers.map { user -> async { repository.deleteUser(user.id) } }.awaitAll()
    }

    suspend  fun getValidUser(users: List<User>?, authResult: AuthResult): User? {
        val duplicatedUsers = getDuplicatedUser(users, authResult)
        // new user login
        Timber.tag(Constants.appName).i("[MakeUniqueAccount] getValidUser , duplicatedUser ${duplicatedUsers.toString()}")
        if (duplicatedUsers.isNullOrEmpty()) {
            Timber.tag(Constants.appName).i(" [MakeUniqueAccount] getValidUser , duplicateUser is nullorEmpty so return ${authResult.data}")
            return authResult.data
        }
        return coroutineScope {
            deleteDuplicatedUser(duplicatedUsers)
            getMergeUser(duplicatedUsers)
        }
    }

    fun getMergeUser(validUsers: List<User>): User {
        return User(
            id = validUsers.mapNotNull { it.id.takeIf { id -> id.isNotEmpty() } }.maxOrNull() ?: "",
            name = validUsers.mapNotNull { it.name.takeIf { name -> name.isNotEmpty() } }.maxOrNull() ?: "",
            email = validUsers.mapNotNull { it.email.takeIf { email -> email.isNotEmpty() } }.maxOrNull() ?: "",
            image = validUsers.mapNotNull { it.image.takeIf { image -> image.isNotEmpty() } }.maxOrNull() ?: ""
        )
    }


    suspend fun getValidAuthResult(
        user: User?
    ): AuthResult {
        return if (user == null) {
            Timber.tag(Constants.appName).i("[MakeUniqueAccount] getValidAuthResult , user is null")
            AuthResult(
                data = null,
                errorMessage = "Operation cannot succeed due to some error"
            )
        } else {
            Timber.tag(Constants.appName).i("[MakeUniqueAccount] getValidAuthResult , user is ${user.toString()}")
            repository.saveUser(user).fold(
                onSuccess = {
                    AuthResult(data = user, errorMessage = null)
                },
                onFailure = {
                    AuthResult(
                        data = null,
                        errorMessage = "Operation cannot succeed due to some error"
                    )
                }
            )
        }
    }


    suspend operator fun invoke(
        users : List<User>? ,
        authResult: AuthResult
    ): AuthResult{
        if(authResult.data == null) return authResult
        val user = getValidUser(
            users = users,
            authResult = authResult
        )
        Timber.tag(Constants.appName).i("[MakeUniqueAccount] invoke : ${user}")
        val validAuthResult = getValidAuthResult(
            user = user
        )
        Timber.tag(Constants.appName).i("[MakeUniqueAccount] validAuthResult :${validAuthResult}")
        return validAuthResult
    }
}


/**
 *
 *
 * Understanding mapNotNull
 * mapNotNull is like map, but it ignores null values.
 * It applies a transformation (transform: (T) -> R?) to each element.
 * If the transformation result is null, it is skipped.
 * The final list contains only non-null results.
 *
 *
 * val numbers = listOf(1, -2, 3, -4, 5)
 *
 * val positiveStrings = numbers.mapNotNull { num ->
 *     if (num > 0) num.toString() else null
 * }
 *
 * println(positiveStrings) // Output: ["1", "3", "5"]
 *
 *
 * Explanation:
 * mapNotNull iterates over each element in numbers.
 * If num > 0, it converts it to a string.
 * If num <= 0, it returns null, so those values are skipped.
 * The final list only contains ["1", "3", "5"] (no null values).
 *
 *
 *
 *
 * maxOrNull() is a Kotlin extension function that finds the largest element in a list. If the list is empty, it returns null.
 * It works only with types that implement Comparable<T> (like Int, Double, String, etc.).
 *
 *
 * val numbers = listOf(5, 10, 3, 8)
 * val maxNumber = numbers.maxOrNull()  // ✅ Returns 10
 *
 * val emptyList = listOf<Int>()
 * val maxEmpty = emptyList.maxOrNull() // ✅ Returns null
 *
 * println(maxNumber)  // Output: 10
 * println(maxEmpty)   // Output: null
 *
 *
 * takeIf() Explained
 * takeIf() is a Kotlin extension function that returns the object itself if it satisfies a given
 * condition (predicate). If the condition is false, it returns null.
 * This is useful when you want to conditionally keep a value or discard it.
 * val number = 10
 *
 * val result = number.takeIf { it > 5 }  // ✅ number is greater than 5, so returns 10
 * val result2 = number.takeIf { it < 5 } // ❌ number is not less than 5, so returns null
 *
 * println(result)  // Output: 10
 * println(result2) // Output: null
 *
 */
