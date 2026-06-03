package com.example.meerkat.feature_meetings.data.data_source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.meerkat.core.calendar.data.data_source.local.BaseDao
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventContactCrossRef
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventWithContacts
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import kotlinx.coroutines.flow.Flow

@Dao
interface EventContactCrossRefDao  : BaseDao<EventContactCrossRef> {

    @Transaction
    @Query("""
    SELECT * FROM ${ConstantCalendar.EVENT_ENTITY_TABLE}
    ORDER BY start_dateTime ASC
""")
    fun getEventsWithContacts(): Flow<List<EventWithContacts>>



    @Transaction
    @Query("SELECT * FROM ${ConstantCalendar.EVENT_ENTITY_TABLE} WHERE ${ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY}=:eventId")
    fun getEventWithContact(eventId : Long) : Flow<EventWithContacts?>
}