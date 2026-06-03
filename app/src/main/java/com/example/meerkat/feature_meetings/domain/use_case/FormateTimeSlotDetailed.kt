package com.example.meerkat.feature_meetings.domain.use_case

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class FormateTimeSlotDetailed {

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(slot: TimeSlot) : Pair<String , String>{
        val dateFormatter = DateTimeFormatter.ofPattern("E, MMMM d, yyyy", Locale.ENGLISH)
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)

        val dateStr = slot.startTime.format(dateFormatter)
        val startTimeStr = slot.startTime.format(timeFormatter)
        val endTimeStr = slot.endTime.format(timeFormatter)

        val offset: ZoneOffset = slot.startTime.offset
        val offsetStr = offset.id.replace("Z", "+00:00") // In case it's UTC

        return Pair(
            dateStr,  // e.g. "Wed, July 30, 2025"
            "$startTimeStr - $endTimeStr (GMT $offsetStr)" // e.g. "10:30 AM - 11:00 AM (GMT +05:00)"
        )
    }
}