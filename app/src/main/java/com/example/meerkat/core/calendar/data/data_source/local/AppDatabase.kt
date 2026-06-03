package com.example.meerkat.core.calendar.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.meerkat.core.calendar.data.data_source.model.local.AttachmentsEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.AttendeeEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.CalendarEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.ConferenceDataEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.Converters
import com.example.meerkat.core.calendar.data.data_source.model.local.EntryPointEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.OverrideEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.ReminderEntity
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.data.data_source.local.ContactDao
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventContactCrossRef
import com.example.meerkat.feature_meetings.data.data_source.local.EventContactCrossRefDao

@Database(
    entities = [
        CalendarEntity::class,
        EventEntity::class,
        AttendeeEntity::class,
        AttachmentsEntity::class,
        ConferenceDataEntity::class,
        EntryPointEntity::class ,
        OverrideEntity::class ,
        ReminderEntity::class ,
        ContactEntity::class ,
        EventContactCrossRef::class ,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao
    abstract fun eventDao(): EventDao
    abstract fun attendeeDao() : AttendeeDao
    abstract fun entryPointDao() : EntryPointDao
    abstract fun attachmentDao() : AttachmentDao
    abstract fun conferenceDataDao() : ConferenceDataDao
    abstract fun overrideDao() : OverrideDao
    abstract fun reminderDao() : ReminderDao
    abstract fun contactDao() : ContactDao
    abstract fun eventContactCrossRefDao() : EventContactCrossRefDao
}