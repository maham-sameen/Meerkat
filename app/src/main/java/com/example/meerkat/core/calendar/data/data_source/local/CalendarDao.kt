package com.example.meerkat.core.calendar.data.data_source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.meerkat.core.calendar.data.data_source.model.local.CalendarEntity
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao  : BaseDao<CalendarEntity> {

    @Transaction
    @Query("DELETE FROM ${ConstantCalendar.CALENDAR_ENTITY_TABLE}")
     suspend fun clearAll()


     @Transaction
     @Query("SELECT * FROM ${ConstantCalendar.CALENDAR_ENTITY_TABLE}")
     fun getAllCalendars(): Flow<List<CalendarEntity>>
}