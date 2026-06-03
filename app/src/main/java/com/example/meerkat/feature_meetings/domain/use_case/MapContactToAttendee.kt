package com.example.meerkat.feature_meetings.domain.use_case

import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee

class MapContactToAttendee {

    operator fun invoke(contact: ContactEntity): MeetingAttendee {
        val initials = contact.name
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercaseChar() }
            .joinToString("")

        return MeetingAttendee(
            profileText = initials,
            name = contact.name,
            isHost = contact.isHost
        )
    }
}