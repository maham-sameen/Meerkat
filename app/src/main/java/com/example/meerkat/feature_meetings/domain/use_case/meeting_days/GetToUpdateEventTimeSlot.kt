package com.example.meerkat.feature_meetings.domain.use_case.meeting_days

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.model.local.toTimeSlot
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import timber.log.Timber

class GetToUpdateEventTimeSlot(
    private val meetingRepo: MeetingRepo
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(eventId : Long): TimeSlot?{
        Timber.tag(Constants.appName).i("\uD83C\uDF49 [GetToUpdateEventTimeSlot]  , eventId : $eventId")
        return meetingRepo.getEventEntityByEventId(eventId)?.toTimeSlot()
    }
}