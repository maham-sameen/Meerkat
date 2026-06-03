package com.example.meerkat.feature_meetings.domain.repository

import androidx.work.WorkManager
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyEventDto
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventContactCrossRef
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventWithContacts
import com.example.meerkat.feature_meetings.domain.model.EmailMessage
import com.example.meerkat.feature_meetings.domain.model.Message
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import kotlinx.coroutines.flow.Flow

interface MeetingRepo {

    fun getAllContact(userId : String) : Flow<List<ContactEntity>>

    suspend fun getCurrentUser(): Result<User>


    suspend fun saveEventWithContact(
        eventContactCrossRef: EventContactCrossRef
    )

    fun getEventWithContacts (timeZone : String) : Flow<List<EventWithContacts>>

    fun getEventWithContactByEventId(eventId : Long) : Flow<EventWithContacts?>

    fun sendEmails(
        emailToEmailMessage : List<EmailMessage>
    )

    suspend fun getEventEntityByEventId(eventId : Long) : EventEntity?

    suspend fun deleteEventEntityByEventId(eventId : Long) : Int

    suspend fun informGuest(messages : List<EmailMessage> , workManager: WorkManager)
}