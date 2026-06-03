package com.example.meerkat.feature_contacts.domain.use_case

import com.example.meerkat.feature_contacts.domain.repository.ContactRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCurrentUserId (
    private val contactRepo: ContactRepo
){
    suspend operator fun invoke(): Result<String> {
        println("\uD83C\uDF82 [GetCurrentUserId] ")
        return contactRepo.getCurrentUser().fold(
            onSuccess = { user ->
                println("\uD83C\uDF82 [GetCurrentUserId] , user : ${user.toString()}")
                Result.success(user.id)
            },
            onFailure = { throwable ->
                println("\uD83C\uDF82 [GetCurrentUserId] , throwable : ${throwable.toString()}")
                Result.failure(throwable)
            }
        )
    }
}