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
        Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] , eventId : $eventId , host ${host.toString()}")
        return meetingRepo.getEventWithContactByEventId(eventId)
            .mapLatest { eventWithContact ->
                Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] , eventWithContact : ${eventWithContact.toString()}")
                if (eventWithContact == null) return@mapLatest null

                val event = eventWithContact.event
                val hangoutLink = event.hangoutLink ?: return@mapLatest null

                val (contactAttendees, meetingAttendees) = eventWithContact.contacts
                    .map { contact ->
                        val attendee = mapContactToAttendee(contact)
                        ContactAttendee(contact, attendee) to attendee
                    }
                    .unzip()

                Timber.tag(Constants.appName).i("\uD83D\uDE97 [GetMeetingDetailsUseCase] , contactAttendee : $contactAttendees , meetingAttendee : $meetingAttendees ")

                val contactAttendeesSnapshotsList = mutableStateListOf<ContactAttendee>().apply {
                    addAll(contactAttendees)
                }

                AMeetingDetails(
                    meerkatEventLinks = getMeerkatEventLinks.generateJoinLinkPair(
                        event.eventId,
                        hangoutLink
                    ),
                    timeDetail = formateTimeSlotDetailed(event.toTimeSlot()),
                    meetingDescription = meetingDescription(meetingAttendees),
                    attendees = contactAttendeesSnapshotsList,
                    host = host,
                    eventId = eventId
                )
            }
    }
}