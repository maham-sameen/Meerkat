package com.example.meerkat.feature_notification.domain.repository

import androidx.paging.PagingData
import com.example.meerkat.feature_notification.domain.model.NotificationEvent
import kotlinx.coroutines.flow.Flow


interface NotificationRepo {
    fun getUpcomingNotificationEvents(timeZone : String): Flow<PagingData<NotificationEvent>>

    suspend fun getTimeZone(): Result<String>
}