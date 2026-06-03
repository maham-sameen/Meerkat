package com.example.meerkat.feature_meetings.presentation.confirm_meeting

import com.example.meerkat.feature_meetings.domain.util.TimeSlot


sealed class ConfirmMeetingEvents {

    data class OnSearchText(val text : String) : ConfirmMeetingEvents()

    data object OnSearchTextDone : ConfirmMeetingEvents()

    data class OnSelectGuest(val contactAttendee: ContactAttendee) : ConfirmMeetingEvents()

    data class OnUnSelectGuest(val contactAttendee: ContactAttendee) : ConfirmMeetingEvents()

    data object ScheduleMeeting : ConfirmMeetingEvents()

    data class SetTimeDetail(val slot : TimeSlot) : ConfirmMeetingEvents()

    data class ToUpdateConfirmMeeting(val eventId : Long): ConfirmMeetingEvents()

    data object OnDismiss : ConfirmMeetingEvents()
}