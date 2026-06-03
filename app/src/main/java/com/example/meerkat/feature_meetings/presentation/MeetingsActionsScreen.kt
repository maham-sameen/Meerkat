package com.example.meerkat.feature_meetings.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class MeetingsActionsScreen {


    @Serializable
    data class MeetingDays(val eventId : Long? = null) : MeetingsActionsScreen()

    @Serializable
    data class ConfirmMeeting(val timeSlotJson : String , val eventId : Long? = null) : MeetingsActionsScreen()


    @Serializable
    data class ScheduleMeetingDetails(val eventId : Long) : MeetingsActionsScreen()

}