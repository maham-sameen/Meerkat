package com.example.meerkat.core.calendar.domain.model

data class EventData(
val id : Long ,
val title: String,
val description: String?,
val startDateTime: String,
val endDateTime: String,
val timeZone: String,
val link: String?,
val location: String?,
val platform: String?, // e.g., "Google Meet", "Calendly"
val status: String?,   // e.g., "confirmed", "cancelled"
val organizerName: String?,
val attendees: List<String> // emails or names
)