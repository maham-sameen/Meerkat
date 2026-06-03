package com.example.meerkat.feature_contacts.data.data_source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.meerkat.core.calendar.data.data_source.local.BaseDao
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.data.data_source.util.ConstantContact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao  : BaseDao<ContactEntity> {

    @Transaction
    @Query("SELECT * FROM ${ConstantContact.CONTACT_ENTITY_TABLE} WHERE ${ConstantContact.CONTACT_ENTITY_USER_ID}= :userId")
    fun getAllContactsFlow(userId : String): Flow<List<ContactEntity>>
}