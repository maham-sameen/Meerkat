package com.example.meerkat.feature_contacts.domain.repository

import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import kotlinx.coroutines.flow.Flow

interface ContactRepo {
    suspend fun insert(contactEntity: ContactEntity)

     fun getAllContact(userId : String) : Flow<List<ContactEntity>>

    suspend fun getCurrentUser(): Result<User>
}