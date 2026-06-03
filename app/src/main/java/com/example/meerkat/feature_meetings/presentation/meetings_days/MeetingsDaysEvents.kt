package com.example.meerkat.feature_meetings.presentation.meetings_days

import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import java.time.LocalDate

sealed class MeetingsDaysEvents {
    data class SelectADay(val index : Int) : MeetingsDaysEvents()
    data class SelectTimeSlot (val index: Int) : MeetingsDaysEvents()
    data class SelectNewDate(val data : LocalDate): MeetingsDaysEvents()
    data class OnToggleSelection(
        val selectedOption : TimeZoneInfo
    ): MeetingsDaysEvents()
    data class OnSearchTxt(
        val text : String
    ): MeetingsDaysEvents()
    object OnDone : MeetingsDaysEvents()

    object DismissCustomizeEvent : MeetingsDaysEvents()
    data object DismissDialog : MeetingsDaysEvents()
    data object ShowDialog : MeetingsDaysEvents()
}