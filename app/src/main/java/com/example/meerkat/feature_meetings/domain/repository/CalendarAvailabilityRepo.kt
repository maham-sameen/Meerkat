package com.example.meerkat.feature_meetings.domain.repository

import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyEventDto
import com.example.meerkat.feature_meetings.data.data_source.model.remote.FreeBusyRequest
import com.example.meerkat.feature_meetings.domain.model.BusyTime

interface CalendarAvailabilityRepo {
    suspend  fun fetchFreeBusyTimes(request: FreeBusyRequest) : Result<List<BusyTime>>

    suspend fun fetchUserTimeZone(): Result<String>

    suspend fun fetchHost() : Result<String>

    suspend fun insertEventRemote(event : MyEventDto) : Result<MyEventDto>

    suspend fun deleteEventRemote(eventId : String) : Result<Unit>

    suspend  fun updateEventRemote(
        eventId : String ,
        event : MyEventDto
    ) : Result<MyEventDto>


    suspend fun getAnEventRemote(eventId : String) : Result<MyEventDto>
}
