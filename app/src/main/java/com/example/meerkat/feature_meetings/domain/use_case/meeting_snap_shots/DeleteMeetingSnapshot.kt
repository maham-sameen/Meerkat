package com.example.meerkat.feature_meetings.domain.use_case.meeting_snap_shots

import com.example.meerkat.Constants
import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import timber.log.Timber


class DeleteMeetingSnapshot(
    private val calendarAvailabilityRepo: CalendarAvailabilityRepo ,
    private val meetingRepo: MeetingRepo
) {
    suspend operator fun invoke(eventId : Long) : Result<String>{
        Timber.tag(Constants.appName).i("\uD83E\uDEDB [GetMeetingSnapShots] , eventId : $eventId")
       return meetingRepo.getEventEntityByEventId(eventId)?.let { eventEntity ->
           Timber.tag(Constants.appName).i("\uD83E\uDEDB [GetMeetingSnapShots] , getEventEntityByEventId , eventEntity : $eventEntity")
           calendarAvailabilityRepo.deleteEventRemote(eventEntity.eventId)
               .fold(
                   onSuccess = {
                       Timber.tag(Constants.appName).i("\uD83E\uDEDB [GetMeetingSnapShots] , deleteEventRemote : ${it.toString()}")
                      val delResult =  meetingRepo.deleteEventEntityByEventId(eventEntity.eventPrimaryId)
                       if(delResult > 0 ) Result.success("Meeting has Cancelled Successfully")
                       else Result.failure(Exception("Something went wrong"))
                   },
                   onFailure = {
                       Timber.tag(Constants.appName).i("\uD83E\uDEDB [GetMeetingSnapShots] , deleteEventRemote , onFailure : ${it.toString()}")
                       Result.failure(it)
                   }
               )
       } ?:Result.failure(Exception("Something went wrong"))
    }
}