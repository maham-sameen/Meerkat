package com.example.meerkat.feature_notification.domain.use_case

import com.example.meerkat.feature_notification.domain.repository.NotificationRepo

class GetTimeZoneUseCase(
    private val notificationRepo: NotificationRepo
) {

    suspend operator fun invoke(): Result<String>{
        return notificationRepo.getTimeZone()
    }
}