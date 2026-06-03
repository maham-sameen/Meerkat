package com.example.meerkat.feature_meetings.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.WorkManager
import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.utils.currentTimeInRfc3339
import com.example.meerkat.core.calendar.domain.repository.EventRepo
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.domain.repository.ContactRepo
import com.example.meerkat.feature_meetings.data.data_source.local.EventContactCrossRefDao
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventContactCrossRef
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventWithContacts
import com.example.meerkat.feature_meetings.data.data_source.remote.EmailRemoteDataSource
import com.example.meerkat.feature_meetings.domain.model.EmailMessage
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class MeetingRepoImp(
    private val contactRepo: ContactRepo,
    private val eventContactCrossRefDao: EventContactCrossRefDao,
    private val eventRepo: EventRepo,
    private val emailRemoteDataSource: EmailRemoteDataSource,
) : MeetingRepo {
    override fun getAllContact(userId: String): Flow<List<ContactEntity>> {
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] , getAllContact() , userId : ${userId.toString()}")
       return contactRepo.getAllContact(userId)
    }

    override suspend fun getCurrentUser(): Result<User> {
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] , getCurrentUser()")
        return contactRepo.getCurrentUser()
    }

    override suspend fun saveEventWithContact(eventContactCrossRef: EventContactCrossRef) {
       val long =  eventContactCrossRefDao.upsert(eventContactCrossRef)
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] saveEventWithContact ${eventContactCrossRef.toString()}, long : $long")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun getEventWithContacts(timeZone : String): Flow<List<EventWithContacts>>{
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] , getEventWithContacts , timeZone : $timeZone")
        return eventContactCrossRefDao.getEventsWithContacts()
    }

    override fun getEventWithContactByEventId(eventId: Long): Flow<EventWithContacts?> {
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] , getEventWithContactByEventId : eventId : ${eventId.toString()}")
        return eventContactCrossRefDao.getEventWithContact(eventId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun sendEmails(emailToEmailMessage: List<EmailMessage>) {
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] sendEmail : ${emailToEmailMessage.toString()}")
        emailToEmailMessage.forEach {
            Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] sendEmail ${it.toString()}")
            emailRemoteDataSource
                .sendEmail(
                    toSendEmail = it.email,
                    message = it.message
                )
        }
    }

    override suspend fun getEventEntityByEventId(eventId: Long): EventEntity? {
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] , getEventEntityByEventId : eventId : ${eventId.toString()}")
        return eventRepo.getEventById(eventId)
    }

    override suspend fun deleteEventEntityByEventId(eventId: Long): Int {
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] , deleteEventEntityByEventId : eventId : $eventId")
        return eventRepo.deleteEventByEventId(eventId)
    }

    override suspend fun informGuest(messages: List<EmailMessage> , workManager: WorkManager) {
        Timber.tag(Constants.appName).i("\uD83C\uDF36 [MeetingRepoImp] inform guest :---> message ${messages.toString()}.")
        emailRemoteDataSource.sendEmailToWorker(
            messages,
            workManager = workManager
        )
    }
}