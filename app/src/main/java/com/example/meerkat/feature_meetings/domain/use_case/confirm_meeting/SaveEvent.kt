package com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting

import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.mapper.AttachmentDtoToAttachmentEntityMapper
import com.example.meerkat.core.calendar.data.data_source.mapper.AttendeeDtoToAttendeeEntityMapper
import com.example.meerkat.core.calendar.data.data_source.mapper.ConferenceDataDtoToConferenceDataEntityMapper
import com.example.meerkat.core.calendar.data.data_source.mapper.EntryPointDtoToEntryPointEntityMapper
import com.example.meerkat.core.calendar.data.data_source.mapper.EventDtoToEventEntityMapper
import com.example.meerkat.core.calendar.data.data_source.mapper.OverrideDtoToOverrideEntityMapper
import com.example.meerkat.core.calendar.data.data_source.model.local.ReminderEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.AttachmentsDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.AttendeeDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.ConferenceDataDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyEventDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.OverridesDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.SourceDto
import com.example.meerkat.core.calendar.domain.repository.CalendarRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Provider

class SaveEvent(
    private val calendarRepo: Provider<CalendarRepo>,
) {

    val repo get() = calendarRepo.get()

    suspend  operator fun invoke(event : MyEventDto)  = coroutineScope {
        Timber.tag(Constants.appName).i("\uD83D\uDCA3 [SaveEvent] , event : ${event.toString()}")
        val eventDtoToEventEntityMapper = EventDtoToEventEntityMapper()
        async {
            val eventId = repo.upsert(
                eventDtoToEventEntityMapper.map(
                    Pair(
                        first = event,
                        second = SourceDto("", ""),
                    )
                )
            )
            Timber.tag(Constants.appName).i("\uD83D\uDCA3 , [SaveEvent]  , eventId : $eventId , insert Conference : ${event.conferenceData}")
            if (event.conferenceData != null) {
                insertConferenceData(event.conferenceData, eventId)
            }
            Timber.tag(Constants.appName).i("\uD83D\uDCA3 , [SaveEvent] , attendees : ${event.attendees.toString()}")
            insertAttendees(event.attendees, eventId)
            Timber.tag(Constants.appName).i("\uD83D\uDCA3 , [SaveEvent] , attachments : ${event.attachments.toString()}")
            insertAttachments(event.attachments, eventId)

            // ✅ Fix begins here
            Timber.tag(Constants.appName).i("\uD83D\uDCA3 , [SaveEvent] , reminders : ${event.reminders.toString()}")
            if (event.reminders != null) {
                val reminderId = repo.upsert(
                    ReminderEntity(
                        reminderOwnerId = eventId,
                        useDefault = event.reminders.useDefault
                    )
                )
                insertOverrides(
                    overridesList = event.reminders.overrides,
                    overrideOwnerId = reminderId,
                )
            }
            eventId
        }
    }



    suspend fun insertAttendees(attendees: List<AttendeeDto>?, attendeeOwnerId: Long) =
        coroutineScope {
            val attendeeDtoToAttendeeEntityMapper = AttendeeDtoToAttendeeEntityMapper()
            attendees
                ?.map { attendee ->
                    async {
                        repo.upsert(
                            attendeeDtoToAttendeeEntityMapper.map(
                                Pair(
                                    attendee,
                                    attendeeOwnerId
                                )
                            )
                        )
                    }
                }
                ?.awaitAll()
        }

    suspend fun insertAttachments(attachments: List<AttachmentsDto>?, attachmentOwnerId: Long) =
        coroutineScope {
            val attachmentDtoToAttachmentEntityMapper = AttachmentDtoToAttachmentEntityMapper()
            attachments
                ?.map { attachment ->
                    async {
                        repo.upsert(
                            attachmentDtoToAttachmentEntityMapper.map(
                                Pair(
                                    attachment,
                                    attachmentOwnerId
                                )
                            )
                        )
                    }
                }
                ?.awaitAll()
        }

    suspend fun insertConferenceData(conferenceData: ConferenceDataDto, conferenceOwnerId: Long) =
        coroutineScope {
            val conferenceDataDtoToConferenceDataEntityMapper =
                ConferenceDataDtoToConferenceDataEntityMapper()
            val entryPointDtoToEntryPointEntityMapper = EntryPointDtoToEntryPointEntityMapper()
            val conferenceDataId = repo.upsert(
                conferenceDataDtoToConferenceDataEntityMapper.map(
                    Pair(
                        conferenceData,
                        conferenceOwnerId
                    )
                )
            )
            conferenceData.entryPoints
                ?.map { entryPoint ->
                    async {
                        repo.upsert(
                            entryPointDtoToEntryPointEntityMapper.map(
                                Pair(
                                    entryPoint,
                                    conferenceDataId
                                )
                            )
                        )
                    }
                }
                ?.awaitAll()
        }


    suspend fun insertOverrides(overridesList : List<OverridesDto>?, overrideOwnerId : Long) =
        coroutineScope {
            val overrideDtoToOverrideEntityMapper = OverrideDtoToOverrideEntityMapper()
            overridesList?.map { override ->
                async {
                    repo.upsert(
                        overrideDtoToOverrideEntityMapper.map(
                            Pair(
                                override,
                                overrideOwnerId
                            )
                        )
                    )
                }
            }
                ?.awaitAll()
        }
}