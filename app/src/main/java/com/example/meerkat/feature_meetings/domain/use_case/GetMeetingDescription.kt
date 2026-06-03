package com.example.meerkat.feature_meetings.domain.use_case

import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee

class GetMeetingDescription {
    operator fun  invoke(attendees: List<MeetingAttendee>): String {
        val nonHosts = attendees.filterNot { it.isHost }.map { it.name }
        return if (nonHosts.isNotEmpty()) {
            "Meeting with ${nonHosts.joinToString(", ")}"
        } else {
            "Meeting with you only"
        }
    }
}