package com.example.meerkat.feature_meetings.presentation.confirm_meeting

import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee

data class UserAttendee(
    val user : User ,
    val attendee: MeetingAttendee
)
