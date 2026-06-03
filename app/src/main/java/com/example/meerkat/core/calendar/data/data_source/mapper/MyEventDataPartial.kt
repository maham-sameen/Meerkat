package com.example.meerkat.core.calendar.data.data_source.mapper

data class MyEventDataPartial(
    val id : Long ,
    val title: String,
    val description: String?,
    val startDateTime: String,
    val endDateTime: String,
    val timeZone: String,
    val link: String?,
    val location: String?,
    val platform: String?,
    val status: String?,
    val organizerName: String?
)