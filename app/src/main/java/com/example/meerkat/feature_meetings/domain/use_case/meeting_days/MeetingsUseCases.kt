package com.example.meerkat.feature_meetings.domain.use_case.meeting_days

import com.example.meerkat.core.time_zone.domain.use_case.GetTimeZonesUseCase
import com.example.meerkat.feature_meetings.domain.use_case.GetHost
import com.example.meerkat.feature_meetings.domain.use_case.GetTZCalender

data class MeetingsUseCases(
    val getBusyTimeList: GetBusyTimeList,
    val getMeetingDates: GetMeetingDates,
    val getTZCalender: GetTZCalender,
    val getTimeZonesUseCase: GetTimeZonesUseCase,
    val getHost: GetHost ,
    val toUpdateMeetings: GetToUpdateEventTimeSlot
)