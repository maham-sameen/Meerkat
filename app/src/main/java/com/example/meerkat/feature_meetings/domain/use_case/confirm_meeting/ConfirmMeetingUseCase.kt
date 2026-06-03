package com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting

import com.example.meerkat.feature_meetings.domain.use_case.FormateTimeSlotDetailed
import com.example.meerkat.feature_meetings.domain.use_case.MapContactToAttendee


data class ConfirmMeetingUseCase(
    val getAllContacts: GetAllContacts,
    val getCurrentUser: GetCurrentUser,
    val formateTimeSlotDetailed: FormateTimeSlotDetailed,
    val mapUserToMeetingAttendee: MapUserToMeetingAttendee,
    val mapContactToAttendee: MapContactToAttendee,
    val onScheduleMeeting: OnScheduleMeeting,
    val getToUpdateConfirmMeeting: GetToUpdateConfirmMeeting
)