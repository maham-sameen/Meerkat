package com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting

import com.example.meerkat.Constants
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import timber.log.Timber

class GetCurrentUser(
    private val meetingRepo: MeetingRepo
) {
    suspend operator fun invoke(): Result<User> {
        Timber.tag(Constants.appName).i("\uD83E\uDD59 , [GetCurrentUserId] ")
        return meetingRepo.getCurrentUser()
    }
}