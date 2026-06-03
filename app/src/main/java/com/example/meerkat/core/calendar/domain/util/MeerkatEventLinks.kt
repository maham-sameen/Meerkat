package com.example.meerkat.core.calendar.domain.util

data class MeerkatEventLinks(
    val joinLink: MeerkatJoinLink,
    val cancelLink: String,
    val rescheduleLink: String
)

data class MeerkatJoinLink(
    val meerkatJoinLink: String,
    val originalGoogleMeetLink: String
)
