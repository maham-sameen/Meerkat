package com.example.meerkat.core.calendar.data.data_source.model.local

import androidx.room.Embedded
import androidx.room.Relation
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar

data class EventFull(
    @Embedded val event: EventEntity,

    @Relation(
        parentColumn = ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY, // "eventId"
        entityColumn = ConstantCalendar.CONFERENCE_DATA_ENTITY_TABLE_FOREIGN_KEY // "eventOwnerId"
    )
    val conferenceData: ConferenceDataEntity?,
    @Relation(
        parentColumn = ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY ,
        entityColumn = ConstantCalendar.ATTACHMENT_ENTITY_TABLE_FOREIGN_KEY
    )
    val attachments : List<AttachmentsEntity> = emptyList<AttachmentsEntity>(),

    @Relation(
        parentColumn = ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY ,
        entityColumn = ConstantCalendar.ATTENDEE_ENTITY_FOREIGN_KEY
    )
    val attendees : List<AttendeeEntity> = emptyList<AttendeeEntity>(),
    @Relation(
        parentColumn = ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY ,
        entityColumn = ConstantCalendar.REMINDER_ENTITY_FOREIGN_KEY
    )
    val reminder : ReminderEntity
)



data class EventWithAttendees(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY ,
        entityColumn = ConstantCalendar.ATTENDEE_ENTITY_FOREIGN_KEY
    )
    val attendees : List<AttendeeEntity> = emptyList<AttendeeEntity>()
)