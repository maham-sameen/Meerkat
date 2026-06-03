package com.example.meerkat.feature_meetings.data.data_source.model.remote

data class FreeBusyRequest(
    val timeMin : String ,
    val timeMax : String ,
    val timeZone : String ,
    val items : List<CalendarID>
)

data class CalendarID(
    val id : String
)


