package com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.WorkManager
import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.AttendeeDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.ConferenceDataDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.ConferenceSolutionKeyDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.CreateRequestDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyEventDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyTimeDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.RemindersDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.SourceDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.StatusDto
import com.example.meerkat.core.calendar.domain.use_case.GetMeerkatEventLinks
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_meetings.data.data_source.model.local.EventContactCrossRef
import com.example.meerkat.feature_meetings.domain.model.EmailMessage
import com.example.meerkat.feature_meetings.domain.model.Message
import com.example.meerkat.feature_meetings.domain.model.Person
import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.use_case.ExtractNameFromEmail
import com.example.meerkat.feature_meetings.domain.use_case.GetTZCalender
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ContactAttendee
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

class OnScheduleMeeting(
    private val getTZCalender: GetTZCalender ,
    private val calendarAvailabilityRepo: CalendarAvailabilityRepo ,
    private val meetingRepo: MeetingRepo ,
    private val geMeerkatJoinLink: GetMeerkatEventLinks ,
    private val extractNameFromEmail: ExtractNameFromEmail ,
    private val saveEvent: SaveEvent
) {

    /**
     * here we return the event id because one event id can have many contacts
     * so after that we  can get  the contact list from that event
     */
    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        existedEventId : Long? = null,
        timeSlot: TimeSlot,
        description: String,
        contactAttendee : List<ContactAttendee>,
        workManager: WorkManager
    ) : Result<String> {
        Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] " +
                "timeSlot ${timeSlot.toString()}" +
                "contactAttendee : ${contactAttendee.toString()}" +
                "\n eventId : $existedEventId")
        var existedEventRemote : MyEventDto? = null
        var existedEventDb : EventEntity? = null

        if(existedEventId != null){
            existedEventDb = meetingRepo.getEventEntityByEventId(existedEventId) ?: return Result.failure(
                Exception("No such meeting exist"))
               calendarAvailabilityRepo.getAnEventRemote(existedEventDb.eventId)
                    .fold(
                   onSuccess = {
                       // at that point we are sure both existedEventDb and existedEventRemote are not null
                       existedEventRemote = it
                       Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] getAnEventRemote() existedEventDb : ${existedEventDb.eventId} , existedEventRemote : ${existedEventRemote.id}")
                   },
                   onFailure = {
                       Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] getAnEventRemote() returning to the failure ${it.toString()} ")
                       return Result.failure(it)
                   }
               )
        }
        val userCalendarTimeZoneResult = getTZCalender()
        val guest = contactAttendee.map {
            it.contact
        }
        return userCalendarTimeZoneResult.fold(
            onSuccess = { timeZone ->
                Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] UserCalendarTimeZoneResult : timeZone : success $timeZone")
                val event = insertEvent(
                    existedEventDb = existedEventRemote,
                    timeSlot = timeSlot,
                    description = description,
                    guest = guest,
                    userCalendarTimeZone = timeZone
                )
                Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] UserCalendarTimeZoneResult event to be inserted : ${event.toString()}")
                val eventResult = if (existedEventId == null) {
                    Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] UserCalendarTimeZoneResult , insertEventRemote()   : ${event.toString()}")
                    calendarAvailabilityRepo.insertEventRemote(event)
                } else {
                    Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] UserCalendarTimeZoneResult , updateEventRemote() : ${existedEventRemote.toString()}")
                    calendarAvailabilityRepo.updateEventRemote(
                        eventId  = existedEventRemote?.id ?: return Result.failure(
                            Exception("No such meeting exist")
                        ),
                        event = event
                    )
                }
                eventResult.fold(
                    onSuccess = {responseEventDto ->
                        Timber.tag(Constants.appName).i(" \uD83E\uDE91 [ScheduleMeeting]   eventResult : success $responseEventDto")
                        // do some clean up for existed event
                        if(existedEventId != null){
                            Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] eventResult , existedEventId : ${existedEventId.toString()}")
                            meetingRepo.deleteEventEntityByEventId(existedEventDb?.eventPrimaryId!!)
                        }
                        Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] eventResult, saveEvent : ${responseEventDto.toString()}")
                        val newEventId = saveEvent(responseEventDto).await()
                        guest.forEach { it ->
                            Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] eventResult , saveEventWithContact $it")
                            meetingRepo.saveEventWithContact(
                                EventContactCrossRef(
                                    cId = it.contactId,
                                    eId = newEventId
                                )
                            )
                        }
                        Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] , informGuest")
                        meetingRepo.informGuest(
                            generateEmailToEmailMessage(
                                event = responseEventDto,
                                timeSlot = timeSlot,
                                attendee = guest
                            ) ,
                            workManager
                        )
                        with(NonCancellable){
                            Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] informGuest()  returning the success  \uD83D\uDE06")
                            Result.success("Successfully schedule meeting")
                        }
                    },
                    onFailure = {
                        Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] informGuest : failure ${it.message.toString()}")
                        Result.failure(it)
                    }
                )
            },
            onFailure = {
                Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] UserCalendarTimeZoneResult : onFailure ${it.message} ")
                Result.failure(it)
            }
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateEmailToEmailMessage(
        event : MyEventDto ,
        timeSlot: TimeSlot ,
        attendee: List<ContactEntity>
    ): List<EmailMessage>{
        return attendee.map {
            EmailMessage(it.email  ,
                    getMessage(createMessage(
                        event = event,
                        timeZone = it.timeZoneInfo.zoneName,
                        timeSlot = timeSlot,
                        attendees = attendee
                    )
                    )
            )
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMessage(event: MyEventDto, timeZone: String, timeSlot: TimeSlot, attendees: List<ContactEntity>): Message {
        val formattedTime = formatEventTime(timeSlot.startTime, timeSlot.endTime, timeZone)
        val attendeeList = attendees.map { Person(it.name ,  it.email) }
        val eventHost = Person(extractNameFromEmail(event.organizer?.email ?: "") , (event.organizer?.email ?: ""))
        return Message(
            meetingDescription = event.summary ?: "No description",
            meerkatJoinLink = geMeerkatJoinLink.generateJoinLinkPair(event.id ?: "", event.hangoutLink ?: ""),
            time = formattedTime,
            host = eventHost,
            attendee = attendeeList,
            htmlLink = event.htmlLink ?: ""
        )
    }



    @RequiresApi(Build.VERSION_CODES.O)
     fun insertEvent(
        existedEventDb : MyEventDto? = null ,
        timeSlot: TimeSlot,
        description: String,
        guest : List<ContactEntity>,
        userCalendarTimeZone : String
    ): MyEventDto {
        Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] insertEvent()  : ${existedEventDb.toString()}")
        Timber.tag(Constants.appName).i("\uD83E\uDE91 [ScheduleMeeting] insertEvent()  : timeSlot : ${timeSlot.toString()} , guest : ${guest.toString()} , userCalendarTimeZone : ${userCalendarTimeZone.toString()}")
        val userZoneId = ZoneId.of(userCalendarTimeZone)
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        val startDateTime = timeSlot.startTime
            .withZoneSameInstant(userZoneId)
            .format(formatter)

        val endDateTime = timeSlot.endTime
            .withZoneSameInstant(userZoneId)
            .format(formatter)


        val start = MyTimeDto(
            dateTime = startDateTime,
            timeZone = userCalendarTimeZone
        )

        val end = MyTimeDto(
            dateTime = endDateTime,
            timeZone = userCalendarTimeZone
        )


        val attendees = guest.map {
            AttendeeDto(email = it.email)
        }

        val summary = getMeetingSummary(guest)

        val source = SourceDto(
            url = "https://saadmirza.net/home",
            title = "Meerkat App"
        )

        return MyEventDto(
            kind = "calendar#event",
            etag = existedEventDb?.etag ,
            id = existedEventDb?.id ,
            status = "confirmed",
            htmlLink = null,
            created = null,
            updated = null,
            summary = summary,
            description = description,
            location = "Google Meet",
            colorId = null,
            creator = null,
            organizer = null,
            start = start,
            end = end,
            endTimeUnspecified = false,
            recurringEventId = null,
            originalStartTime = null,
            recurrence = null,
            iCalUID = null,
            transparency = "opaque",
            visibility = "default",
            sequence = existedEventDb?.sequence,
            attendees = attendees,
            attendeesOmitted = false,
            extendedProperties = null,
            hangoutLink = null,
            conferenceData = ConferenceDataDto(
                createRequest = CreateRequestDto(
                    requestId = UUID.randomUUID().toString(),
                    conferenceSolutionKey = ConferenceSolutionKeyDto(type = "hangoutsMeet"),
                    status = StatusDto(statusCode = "pending")
                ),
                notes = "Auto-generated Google Meet link",
            ),
            anyoneCanAddSelf = true,
            guestsCanInviteOthers = true,
            guestsCanModify = false,
            guestsCanSeeOtherGuests = true,
            privateCopy = false,
            locked = false,
            reminders = RemindersDto(
                useDefault = true,
                overrides = null
            ),
            source = source,
            outOfOfficeProperties = null,
            focusTimeProperties = null,
            attachments = null,
            birthdayProperties = null,
            eventType = "default"
        )
    }
    private fun getMeetingSummary(attendees: List<ContactEntity>): String {
        val nonHosts = attendees.filterNot { it.isHost }.map { it.name }
        return if (nonHosts.isNotEmpty()) {
            "Meeting with ${nonHosts.joinToString(", ")}"
        } else {
            "Meeting with you only"
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatEventTime(
        startTime: ZonedDateTime,
        endTime: ZonedDateTime,
        timeZoneId: String
    ): String {
        val userZoneId = ZoneId.of(timeZoneId)

        val startZoned = startTime.withZoneSameInstant(userZoneId)
        val endZoned = endTime.withZoneSameInstant(userZoneId)

        val dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMM yyyy", Locale.ENGLISH)
        val timeFormatter = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH)

        val datePart = startZoned.format(dateFormatter)

        fun shortenTime(timeStr: String): String {
            return if (timeStr.endsWith(":00am") || timeStr.endsWith(":00pm")) {
                timeStr.replace(":00", "")
            } else {
                timeStr
            }
        }

        val startTimeFormatted = shortenTime(startZoned.format(timeFormatter).lowercase())
        val endTimeFormatted = shortenTime(endZoned.format(timeFormatter).lowercase())

        val zoneName = startZoned.zone.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

        return "$datePart · $startTimeFormatted – $endTimeFormatted ($zoneName)"
    }


    private fun getMessage(
        message: Message
    ): Map<String, String> {
        // Format attendees as a nice list with bullets
        val attendeesHtml = message.attendee.joinToString(separator = "") { (name, email) ->
            """
        <li style="margin-bottom: 6px;">
            <strong>$name</strong><br>
            <a href="mailto:$email" style="color: #1a73e8;">$email</a>
        </li>
        """.trimIndent()
        }

        return mapOf(
            "subject" to message.meetingDescription,
            "html" to """
        <div style="font-family: Arial, sans-serif; padding: 20px; color: #202124; background: #f9f9f9;">
            <h2 style="color: #1a73e8; margin-bottom: 8px;">30 Minute Meeting (Powered by Meerkat)</h2>
            <p><a href="https://saadmirza.net/home" target="_blank" style="color: #1a73e8; text-decoration: none;">Open Meeting Details</a></p>
            
            <h3 style="margin-top: 30px; margin-bottom: 6px;">Location</h3>
            <p>This is a Google Meet web conference.</p>
            <p><a href="${message.meerkatJoinLink.originalGoogleMeetLink}" target="_blank" style="color: #1a73e8;">Join with Google Meet</a></p>
            <p><strong>Meeting link:</strong> <a href="${message.meerkatJoinLink.originalGoogleMeetLink}" target="_blank" style="color: #1a73e8;">${message.meerkatJoinLink.meerkatJoinLink}</a></p>

            <hr style="margin: 24px 0; border-color: #ddd;">

            <p><strong>When:</strong><br>${message.time}</p>

            <h3 style="margin-top: 30px;">Guests</h3>
            <p>
                <strong>${message.host.name}</strong> – organiser<br>
                <a href="mailto:${message.host.email}" style="color: #1a73e8;">${message.host.email}</a>
            </p>
            <ul style="list-style-type: disc; margin-left: 20px; color: #444;">
                $attendeesHtml
            </ul>
            <p><a href="${message.htmlLink}" target="_blank" style="color: #1a73e8;">View all guest info</a></p>

            <p style="margin-top: 40px; font-size: 12px; color: #888;">
                —<br>
                Invitation sent via <a href="https://saadmirza.net" target="_blank" style="color: #1a73e8;">Meerkat</a>
            </p>
        </div>
        """.trimIndent()
        )
    }

}

/**
 * So, what to send for MyEventDto during update
 * When eventId != null:
 *
 * 1️⃣ Must send
 * id → from the existing event (existedEventDb.eventId)
 *
 * etag → from existing event (existedEventDb.etag)
 *
 * sequence → from latest Google event (existingEvent.sequence)
 *
 * Any fields you actually want to change (summary, description, attendees, start/end, location, etc.)
 *
 * 2️⃣ Should omit / not send (leave null in DTO)
 * kind, created, updated, iCalUID, htmlLink → these are server-generated; let Google fill them.
 *
 * organizer, creator → server will populate.
 *
 * hangoutLink → Google will regenerate if needed.
 *
 * conferenceData → Only send if you’re creating/updating meet link.
 *
 * Anything you’re not changing and don’t need to overwrite.
 */