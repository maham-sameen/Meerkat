package com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting

import com.example.meerkat.Constants
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import timber.log.Timber

class MapUserToMeetingAttendee {

    operator fun invoke(user: User, isHost: Boolean = false) : MeetingAttendee{
        val profileText = user.name.firstOrNull()?.uppercaseChar()
            ?: user.email.firstOrNull()?.uppercaseChar()
            ?: '?'

        Timber.tag(Constants.appName).i("\uD83E\uDED2  [MapUserToMeetingAttendee] , user : ${user.toString()} , isHost : $isHost ")
        return MeetingAttendee(
            profileText = profileText.toString(),
            name = user.name.ifBlank { user.email },
            isHost = isHost
        )
    }
}