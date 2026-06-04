package com.example.meerkat.feature_meetings.domain.use_case.scheduled_meetings_details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.model.local.toTimeSlot
import com.example.meerkat.core.calendar.domain.use_case.GetMeerkatEventLinks
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.use_case.GetMeetingDescription
import com.example.meerkat.feature_meetings.domain.use_case.FormateTimeSlotDetailed
import com.example.meerkat.feature_meetings.domain.use_case.MapContactToAttendee
import com.example.meerkat.feature_meetings.domain.util.AMeetingDetails
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ContactAttendee
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

class GetMeetingDetails(
    private val meetingRepo: MeetingRepo,
    private val formateTimeSlotDetailed: FormateTimeSlotDetailed,
    private val meetingDescription: GetMeetingDescription,
    private val getMeerkatEventLinks: GetMeerkatEventLinks,
    private val mapContactToAttendee: MapContactToAttendee,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(eventId: Long, host: MeetingAttendee): Flow<AMeetingDetails?> {
        Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] START eventId=$eventId , host=$host")
        return meetingRepo.getEventWithContactByEventId(eventId)
            .mapLatest { eventWithContact ->
                Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] eventWithContact=$eventWithContact")
                if (eventWithContact == null) {
                    Timber.tag(Constants.appName).w("\uD83D\uDE97 [GetMeetingDetailsUseCase] RETURNING null because eventWithContact is null")
                    return@mapLatest null
                }

                val event = eventWithContact.event
                Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] event.eventId=${event.eventId} , event.hangoutLink=${event.hangoutLink} , event.htmlLink=${event.htmlLink} , event.summary=${event.summary}")
                Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] eventWithContact.contacts.size=${eventWithContact.contacts.size}")
                eventWithContact.contacts.forEachIndexed { i, c ->
                    Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] contact[$i] name=${c.name} email='${c.email}' phone='${c.phoneNumber.countryCode}${c.phoneNumber.phoneNumber}' isHost=${c.isHost}")
                }

                val hangoutLink = event.hangoutLink
                if (hangoutLink == null) {
                    Timber.tag(Constants.appName).w("\uD83D\uDE97 [GetMeetingDetailsUseCase] RETURNING null because event.hangoutLink is null")
                    return@mapLatest null
                }

                val (contactAttendees, meetingAttendees) = eventWithContact.contacts
                    .map { contact ->
                        val attendee = mapContactToAttendee(contact)
                        ContactAttendee(contact, attendee) to attendee
                    }
                    .unzip()

                Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] mapped contactAttendees.size=${contactAttendees.size} , meetingAttendees.size=${meetingAttendees.size}")

                val contactAttendeesSnapshotsList = mutableStateListOf<ContactAttendee>().apply {
                    addAll(contactAttendees)
                }

                val links = getMeerkatEventLinks.generateJoinLinkPair(event.eventId, hangoutLink)
                Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] links meerkat='${links.meerkatJoinLink}' google='${links.originalGoogleMeetLink}'")

                val result = AMeetingDetails(
                    meerkatEventLinks = links,
                    timeDetail = formateTimeSlotDetailed(event.toTimeSlot()),
                    meetingDescription = meetingDescription(meetingAttendees),
                    attendees = contactAttendeesSnapshotsList,
                    host = host,
                    eventId = eventId
                )
                Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] EMIT AMeetingDetails attendees.size=${result.attendees.size} host=${result.host.name}")
                result
            }
    }
}