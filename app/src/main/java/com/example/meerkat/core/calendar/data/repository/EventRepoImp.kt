package com.example.meerkat.core.calendar.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.local.EventDao
import com.example.meerkat.core.calendar.data.data_source.mapper.AttendeesEntityToListMapper
import com.example.meerkat.core.calendar.data.data_source.mapper.EntityToMyEventDataMapper
import com.example.meerkat.core.calendar.data.data_source.mapper.EventEntityToPartialEventDataMapper
import com.example.meerkat.core.calendar.data.data_source.model.composite.CompositeDataEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.utils.currentTimeInRfc3339
import com.example.meerkat.core.calendar.domain.model.EventData
import com.example.meerkat.core.calendar.domain.repository.CalendarEntityRepo
import com.example.meerkat.core.calendar.domain.repository.EventRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

class EventRepoImp(
    private val eventDao: EventDao,
) : EventRepo {

    private val entityToMyEventDataMapper: EntityToMyEventDataMapper = EntityToMyEventDataMapper(
        eventMapper = EventEntityToPartialEventDataMapper(),
        attendeesMapper = AttendeesEntityToListMapper()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllEvents(timeZone : String): Flow<PagingData<EventData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = true,
                initialLoadSize = 10
            ),

            pagingSourceFactory = {
                eventDao.pagingSource(currentTimeInRfc3339(timeZone))
            }
        ).flow.mapLatest { pagingData ->
            pagingData.map { entity ->
                entityToMyEventDataMapper.map(
                    CompositeDataEntity(
                        eventEntity = entity.event,
                        attendees = entity.attendees
                    )
                )
            }
        }
    }

    override suspend fun deleteEventByEventId(eventId: Long): Int {
        Timber.tag(Constants.appName).i("\uD83D\uDC24 [EventRepoImp] deleteEventByEventId eventId : $eventId")
       return  eventDao.deleteEventByEventId(eventId)
    }

    override suspend fun getEventById(eventId: Long): EventEntity? {
        Timber.tag(Constants.appName).i("\uD83D\uDC24 [EventRepoImp] getEventByEventId eventId : $eventId")
        return eventDao.getEventByEventId(eventId)
    }
}