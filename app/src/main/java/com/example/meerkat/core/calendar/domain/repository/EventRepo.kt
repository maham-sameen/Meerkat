package com.example.meerkat.core.calendar.domain.repository

import androidx.paging.PagingData
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.domain.model.EventData
import kotlinx.coroutines.flow.Flow

interface EventRepo {
    fun getAllEvents(timeZone : String) : Flow<PagingData<EventData>>

    suspend fun deleteEventByEventId(eventId : Long): Int


   suspend fun getEventById(eventId : Long) : EventEntity?
}