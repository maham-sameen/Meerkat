package com.example.meerkat.feature_meetings.data.data_source.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import com.example.meerkat.feature_contacts.data.data_source.util.ConstantContact
import com.example.meerkat.feature_meetings.data.data_source.util.ConstantsMeeting


@Entity(
    tableName = ConstantsMeeting.EVENT_CONTACT_CROSS_REF_TABLE_NAME,
    primaryKeys = [ConstantsMeeting.EVENT_CONTACT_CROSS_REF_EVENT_ID,
        ConstantsMeeting.EVENT_CONTACT_CROSS_REF_CONTACT_ID],


    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = [ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY],
            childColumns = [ConstantsMeeting.EVENT_CONTACT_CROSS_REF_EVENT_ID],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE
        ),
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = [ConstantContact.CONTACT_ENTITY_PRIMARY_KEY],
            childColumns = [ConstantsMeeting.EVENT_CONTACT_CROSS_REF_CONTACT_ID],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(ConstantsMeeting.EVENT_CONTACT_CROSS_REF_EVENT_ID),
        Index(ConstantsMeeting.EVENT_CONTACT_CROSS_REF_CONTACT_ID)]


)
data class EventContactCrossRef(
    val eId: Long,  // 👈 matches EventEntity's PK
    val cId: Long               // 👈 matches ContactEntity's PK
)