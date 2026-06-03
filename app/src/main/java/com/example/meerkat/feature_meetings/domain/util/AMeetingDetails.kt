package com.example.meerkat.feature_meetings.domain.util

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.meerkat.core.calendar.domain.util.MeerkatJoinLink
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ContactAttendee

data class AMeetingDetails(
    val meerkatEventLinks: MeerkatJoinLink = MeerkatJoinLink("" , ""),
    val timeDetail : Pair<String , String> = "" to "",
    val meetingDescription : String = "",
    val attendees : SnapshotStateList<ContactAttendee> = mutableStateListOf(),
    val host : MeetingAttendee = MeetingAttendee(
        profileText = "",
        name = "",
        isHost = false
    ),
    val eventId : Long = -1L
)
