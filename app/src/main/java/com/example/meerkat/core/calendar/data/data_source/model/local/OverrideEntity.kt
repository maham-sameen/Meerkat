package com.example.meerkat.core.calendar.data.data_source.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar

@Entity(
    tableName = ConstantCalendar.OVERRIDE_ENTITY_TABLE_,
    foreignKeys = [
        ForeignKey(
            entity = ReminderEntity::class ,
            parentColumns = [ConstantCalendar.REMINDER_ENTITY_PRIMARY_KEY],
            childColumns = [ConstantCalendar.OVERRIDE_ENTITY_FOREIGN_KEY_REMINDER]
        )
    ],
    indices = [Index(value = [ConstantCalendar.OVERRIDE_ENTITY_FOREIGN_KEY_REMINDER])]
)
data class OverrideEntity(
    @PrimaryKey(autoGenerate = true) val overrideId: Long = 0,
    val reminderOwnerId : Long? ,
    val method: String,
    val minutes: Int
)

