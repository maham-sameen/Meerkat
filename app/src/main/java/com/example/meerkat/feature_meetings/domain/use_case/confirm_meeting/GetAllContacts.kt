package com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting

import com.example.meerkat.Constants
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.util.distinctUntilListChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetAllContacts(
    private val meetingRepo: MeetingRepo
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(userId: String): Flow<List<ContactEntity>> {
        Timber.tag(Constants.appName).i("\uD83E\uDD59 [GetAllContacts] , result : ${userId.toString()}")
        return meetingRepo
            .getAllContact(userId)
            .distinctUntilListChanged()
    }
}