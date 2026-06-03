package com.example.meerkat.feature_meetings.presentation.schedule_meeting_details

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.meerkat.core.calendar.domain.util.MeerkatEventLinks
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ContactAttendee

data class ScheduleMeetingState(
    val attendees : SnapshotStateList<ContactAttendee>,
    val meerkatEventLinks: MeerkatEventLinks,
)
