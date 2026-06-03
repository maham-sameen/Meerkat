package com.example.meerkat.core.calendar.data.data_source.model.remote

data class CalendarInfoDto(
    val kind: String,
    val etag: String,
    val id: String,
    val summary: String,
    val description: String?,
    val location: String?,
    val timeZone: String?,
)