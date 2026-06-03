package com.example.meerkat.feature_auth.data.repository

import androidx.work.WorkManager
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventContactCrossRef
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventWithContacts
import com.example.meerkat.feature_meetings.domain.model.EmailMessage
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeMeetingRepo : MeetingRepo {
    override fun getAllContact(userId: String): Flow<List<ContactEntity>> {
        return flowOf()
    }

    override suspend fun getCurrentUser(): Result<User> {
        return Result.failure(Exception(""))
    }

    override suspend fun saveEventWithContact(eventContactCrossRef: EventContactCrossRef) {

    }

    override fun getEventWithContacts(timeZone: String): Flow<List<EventWithContacts>> {
       return flowOf()
    }

    override fun getEventWithContactByEventId(eventId: Long): Flow<EventWithContacts?> {
        return flow {  }
    }

    override fun sendEmails(emailToEmailMessage: List<EmailMessage>) {

    }

    override suspend fun getEventEntityByEventId(eventId: Long): EventEntity? {
        return null
    }

    override suspend fun deleteEventEntityByEventId(eventId: Long): Int {
        return 1
    }

    override suspend fun informGuest(
        messages: List<EmailMessage>,
        workManager: WorkManager
    ) {

    }
}