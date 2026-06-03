package com.example.meerkat.feature_meetings.domain.util

data class MeetingDate (
    val  dayName : String ,
    val  dayNumber : Int ,
    val  monthName : String ,
    val  monthNumber : Int ,
    val  yearNumber : Int,
    val  daysOfWeek: DaysOfWeek? = null
)