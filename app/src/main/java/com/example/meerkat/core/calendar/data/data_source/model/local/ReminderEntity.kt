package com.example.meerkat.core.calendar.data.data_source.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar


@Entity(
    tableName = ConstantCalendar.REMINDER_ENTITY_TABLE ,
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class ,
            parentColumns = [ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY] ,
            childColumns = [ConstantCalendar.REMINDER_ENTITY_FOREIGN_KEY],
            onDelete = ForeignKey.CASCADE ,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = [ConstantCalendar.REMINDER_ENTITY_FOREIGN_KEY])]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val reminderId : Long = 0 ,
    val reminderOwnerId : Long ,
    val useDefault : Boolean
)


data class ReminderWithData(
    @Embedded  val reminders: ReminderEntity ,

    @Relation(
        parentColumn = ConstantCalendar.REMINDER_ENTITY_PRIMARY_KEY  ,
        entityColumn = ConstantCalendar.OVERRIDE_ENTITY_FOREIGN_KEY_REMINDER
    )
    val overrides : List<OverrideEntity> = emptyList<OverrideEntity>()
)