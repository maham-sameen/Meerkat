package com.example.meerkat.feature_notification.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingData
import androidx.paging.map
import com.example.meerkat.core.calendar.domain.repository.CalendarEntityRepo
import com.example.meerkat.core.calendar.domain.repository.EventRepo
import com.example.meerkat.feature_notification.data.data_source.mapper.EventDataToNotificationEventMapper
import com.example.meerkat.feature_notification.domain.model.NotificationEvent
import com.example.meerkat.feature_notification.domain.repository.NotificationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepoImp(
    private val eventRepo: EventRepo ,
    private val eventDataToNotificationEventMapper: EventDataToNotificationEventMapper,
    private val calendarEntityRepo: CalendarEntityRepo
) : NotificationRepo {
    @RequiresApi(Build.VERSION_CODES.O)
    override  fun getUpcomingNotificationEvents(timeZone : String): Flow<PagingData<NotificationEvent>> {
        return eventRepo.getAllEvents(timeZone)
            .map { pagingData ->
                pagingData.map{eventData ->
                    println("eventData : $eventData")
                    eventDataToNotificationEventMapper.map(eventData)
                }
            }
    }

    override suspend fun getTimeZone(): Result<String> {
        return calendarEntityRepo.getCalendarInfo().fold(
            onSuccess = { Result.success(it)},
            onFailure = { Result.failure(it)}
        )
    }
}