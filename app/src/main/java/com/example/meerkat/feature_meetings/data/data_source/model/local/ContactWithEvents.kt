package com.example.meerkat.feature_meetings.data.data_source.model.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import com.example.meerkat.feature_contacts.data.data_source.util.ConstantContact
import com.example.meerkat.feature_contacts.domain.model.ContactEntity

data class ContactWithEvents(
    @Embedded val contact: ContactEntity,

    @Relation(
        parentColumn = "contactId",
        entityColumn = "eventPrimaryId",
        associateBy = Junction(EventContactCrossRef::class)
    )
    val events: List<EventEntity>
)
