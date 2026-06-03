package com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting

import com.example.meerkat.Constants
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.use_case.MapContactToAttendee
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ContactAttendee
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

class GetToUpdateConfirmMeeting(
    private val meetingRepo: MeetingRepo ,
    private val mapContactToAttendee: MapContactToAttendee
) {

    /**
     * when we call the _eventId.value  inside ConfirmMeetingViewModel when call onScheduleMeetingUseCase then
     *  Our screen show animation --> Loading Animation in that case our selectGuest flow collect again but after the onScheduleMeetingUseCase
     *  had called means if we are editing the existing event then we have delete that event from database inside that
     *  onScheduleMeetingUseCase  so when we again collect select Guest then GetToUpdateConfirmMeeting called again and there is
     *  no contact for the eventId that was we want to edit meeting so make sure to
     *  add filterNotNUll so that our selectGuest will not be empty .
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(eventId: Long) : Flow<List<ContactAttendee>>
        {
            Timber.tag(Constants.appName).i("\uD83E\uDD59 [GetToUpdateConfirmMeeting] , eventId : $eventId  \uD83D\uDE03 ")
           return meetingRepo.getEventWithContactByEventId(eventId)
               .filterNotNull()
                .mapLatest { eventWithContacts ->
                    Timber.tag(Constants.appName).i("\uD83E\uDD59 [GetToUpdateConfirmMeeting] : contacts : ${eventWithContacts.contacts.toString()} , existedEventId : $eventId")
                    eventWithContacts.contacts.map {
                        Timber.tag(Constants.appName).i("\uD83E\uDD59 [GetToUpdateConfirmMeeting] map ${it.toString()} , eventId : $eventId")
                        ContactAttendee(
                            contact = it,
                            attendee = mapContactToAttendee(it)
                        )
                    }
                }
        }
}