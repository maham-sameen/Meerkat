package com.example.meerkat.core.calendar.domain.repository

import com.example.meerkat.core.calendar.data.data_source.model.local.CalendarEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.CalendarInfoDto
import kotlinx.coroutines.flow.Flow

interface CalendarEntityRepo {

     fun getAllCalendarEntities () : Flow<List<CalendarEntity>>


     suspend fun getCalendarInfo() : Result<String>
}