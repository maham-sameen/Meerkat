package com.example.meerkat.feature_contacts.data.repository

import com.example.meerkat.core.calendar.domain.model.SessionExpiredException
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import com.example.meerkat.feature_contacts.data.data_source.local.ContactDao
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.domain.repository.ContactRepo
import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import kotlinx.coroutines.flow.Flow

class ContactRepoImp(
    private val contactDao: ContactDao ,
    private val userRepo: UserRepo ,
    private val calendarAvailabilityRepo: CalendarAvailabilityRepo
) : ContactRepo {
    override suspend fun insert(contactEntity: ContactEntity) {
        println("\uD83C\uDF82 [ContactRepoImp] , insert() , contactEntity : ${contactEntity.toString()}")
        contactDao.upsert(contactEntity)
    }

    override fun getAllContact(userId : String): Flow<List<ContactEntity>> {
        println("\uD83C\uDF82 [ContactRepoImp] getAllContacts : userId : ${userId.toString()}")
        return contactDao.getAllContactsFlow(userId)
    }

    override suspend fun getCurrentUser(): Result<User> {
        val hostResult = calendarAvailabilityRepo.fetchHost()
        println("\uD83C\uDF82 [ContactRepoImp] getCurrentUser() , hostResult : ${hostResult.toString()}")
        return if (hostResult.isSuccess) {
            val host = hostResult.getOrNull()
            println("\uD83C\uDF82 [ContactRepoImp] , host : ${host.toString()}")
            if (host != null) {
                val user = userRepo.getUserViaEmail(host)
                println("\uD83C\uDF82 [ContactRepoImp] , user : ${user.toString()}")
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(SessionExpiredException())
                }
            } else {
                Result.failure(Exception("Host is null"))
            }
        } else {
            Result.failure(hostResult.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}