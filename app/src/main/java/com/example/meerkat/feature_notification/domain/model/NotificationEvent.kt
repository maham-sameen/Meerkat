package com.example.meerkat.feature_notification.domain.model

data class NotificationEvent(
    val id : Long ,
    val organizer : String?,
    val title: String,
    val timeAgo: String,
    val meetingTitle: String,
    val meetingWith: List<String>,
    val meetingDateTime: String ,
    val timeZoneName : String
)
