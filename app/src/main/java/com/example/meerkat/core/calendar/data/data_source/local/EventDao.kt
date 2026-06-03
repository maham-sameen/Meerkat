package com.example.meerkat.core.calendar.data.data_source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.EventWithAttendees
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao : BaseDao<EventEntity> {

    // update queries
    @Transaction
    @Query("SELECT * FROM ${ConstantCalendar.EVENT_ENTITY_TABLE} WHERE ${ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY} = :eventId LIMIT 1")
    suspend fun getEventByEventId(eventId : Long): EventEntity?

    @Transaction
    @Query("""
    SELECT * 
    FROM ${ConstantCalendar.EVENT_ENTITY_TABLE}
    WHERE start_dateTime >= :currentTime
    ORDER BY start_dateTime ASC
""")
    fun pagingSource(currentTime: String): PagingSource<Int , EventWithAttendees>


    @Transaction
    @Query("DELETE FROM ${ConstantCalendar.EVENT_ENTITY_TABLE} WHERE ${ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY}=:eventId")
    suspend fun deleteEventByEventId(eventId : Long) : Int

}