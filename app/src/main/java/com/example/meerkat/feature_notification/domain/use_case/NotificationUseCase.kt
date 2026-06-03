package com.example.meerkat.feature_notification.domain.use_case

data class NotificationUseCase(
    val getNotificationEvents: GetNotificationEvents ,
    val getTimeZoneUseCase: GetTimeZoneUseCase
)
