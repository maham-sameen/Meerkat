package com.example.meerkat.feature_meetings.presentation.schedule_meeting_details


sealed class ScheduleMeetingDetailsEvents {
    data object DismissDialog : ScheduleMeetingDetailsEvents()
    data object ShowDialog : ScheduleMeetingDetailsEvents()
    data class  OnEventId(val eventId : Long) : ScheduleMeetingDetailsEvents()
}