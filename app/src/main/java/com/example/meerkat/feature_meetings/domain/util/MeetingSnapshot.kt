package com.example.meerkat.feature_meetings.domain.util

import com.example.meerkat.core.calendar.domain.util.MeerkatJoinLink

data class MeetingSnapshot(
    val eventId : Long,
    val meerkatEventLinks: MeerkatJoinLink,
    val timeDetail : Pair<String , String>,
    val meetingDescription : String
)