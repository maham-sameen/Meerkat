package com.example.meerkat.feature_meetings.data.data_source.model.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import com.example.meerkat.feature_contacts.data.data_source.util.ConstantContact
import com.example.meerkat.feature_meetings.data.data_source.util.ConstantsMeeting

data class EventWithContacts(
    @Embedded val event: EventEntity,

    @Relation(
        parentColumn = ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY,
        entityColumn = ConstantContact.CONTACT_ENTITY_PRIMARY_KEY,
        associateBy = Junction(
            value = EventContactCrossRef::class,
            parentColumn = ConstantsMeeting.EVENT_CONTACT_CROSS_REF_EVENT_ID,
            entityColumn = ConstantsMeeting.EVENT_CONTACT_CROSS_REF_CONTACT_ID
        )
    )
    val contacts: List<ContactEntity>
)