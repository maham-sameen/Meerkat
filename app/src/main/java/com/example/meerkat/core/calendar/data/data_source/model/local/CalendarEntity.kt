package com.example.meerkat.core.calendar.data.data_source.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar

@Entity(tableName = ConstantCalendar.CALENDAR_ENTITY_TABLE)
data class CalendarEntity(
    @PrimaryKey(autoGenerate = true) val calendarId: Long = 0,
    val kind: String,
    val etag: String,
    val summary: String,
    val description: String?,
    val timeZone: String?,
    val location : String? ,
    val id : String
)
/**
 * https://developers.google.com/workspace/calendar/api/v3/reference/events ==> if field end with nullable
 *
 * ✅ If a field is marked nullable (val title: String?) and you don’t provide a value:
 * ➤ Room does NOT crash.
 * Room will insert null into the corresponding database column.
 */