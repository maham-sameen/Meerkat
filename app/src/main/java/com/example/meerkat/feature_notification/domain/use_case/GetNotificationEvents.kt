package com.example.meerkat.feature_notification.domain.use_case

import androidx.paging.PagingData
import com.example.meerkat.feature_notification.domain.model.NotificationEvent
import com.example.meerkat.feature_notification.domain.repository.NotificationRepo
import kotlinx.coroutines.flow.Flow

class GetNotificationEvents(
    private val notificationRepo: NotificationRepo
) {

    operator fun invoke(timeZone : String): Flow<PagingData<NotificationEvent>> = notificationRepo.getUpcomingNotificationEvents(timeZone)
}