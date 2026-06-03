package com.example.meerkat.feature_meetings.presentation.confirm_meeting

import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee

data class ContactAttendee(
    val contact: ContactEntity,
    val attendee: MeetingAttendee
)
