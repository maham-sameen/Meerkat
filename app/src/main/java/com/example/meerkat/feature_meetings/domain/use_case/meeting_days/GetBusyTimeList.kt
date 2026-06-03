package com.example.meerkat.feature_meetings.domain.use_case.meeting_days

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.model.local.toTimeSlot
import com.example.meerkat.core.calendar.domain.model.SessionExpiredException
import com.example.meerkat.feature_meetings.data.data_source.model.remote.CalendarID
import com.example.meerkat.feature_meetings.data.data_source.model.remote.FreeBusyRequest
import com.example.meerkat.feature_meetings.domain.model.BusyTime
import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.util.MeetingDate
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import timber.log.Timber
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GetBusyTimeList(
    private val calendarAvailabilityRepo: CalendarAvailabilityRepo ,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        timeZone : String,
        meetingDate: MeetingDate,
        onSessionExpired : () -> Unit ): List<TimeSlot>{
        Timber.tag(Constants.appName).i("\uD83C\uDF49 [GetBusyTimeList] , timeZone : $timeZone , meetingDate : $meetingDate")
                val timeRange = meetingDate.toOfficeHoursRange(timeZone)
                val request = FreeBusyRequest(
                    timeMin = timeRange.first,
                    timeMax = timeRange.second,
                    timeZone = "UTC",
                    items = listOf(CalendarID("primary"))
                )
                val busyListResult = calendarAvailabilityRepo.fetchFreeBusyTimes(request)
                if(busyListResult.isSuccess){
                    val  busyList  = busyListResult.getOrNull()
                    if(busyList != null){
                        return  generateFreeSlotsForOfficeTime(
                            date = meetingDate,
                            busyList = busyList,
                            timeZone = timeZone,
                            intervalMinutes = 30
                        )
                    }
                }else{
                    if(busyListResult.exceptionOrNull() is SessionExpiredException){
                        onSessionExpired()
                    }
                }
        return  emptyList()
    }

    /**
     * Sure bro, here’s a **simple and clear explanation** of what your `generateFreeSlots()` function does 👇:
     *
     * ---
     *
     * ## 🧠 Purpose:
     *
     * This function creates a list of **free time slots** (e.g., 30-minute blocks) for a full day, skipping any time ranges that are marked as **busy**.
     *
     * ---
     *
     * ## ✅ Function Parameters:
     *
     * ```kotlin
     * generateFreeSlots(
     *     date: MeetingDate,           // the day we want to generate slots for
     *     busyList: List<BusyTime>,    // list of busy time periods (from Google Calendar, etc.)
     *     timeZone: String,            // user’s local time zone like "Asia/Karachi"
     *     intervalMinutes: Long = 30   // length of each free slot (default is 30 minutes)
     * )
     * ```
     *
     * ---
     *
     * ## 🔁 Step-by-step Breakdown:
     *
     * ### 1. **Start & End of Day**:
     *
     * ```kotlin
     * val startOfDay = ZonedDateTime.of(...)
     * val endOfDay = startOfDay.plusDays(1)
     * ```
     *
     * ➡️ Defines **midnight 12:00 AM to next 12:00 AM** in the user's time zone for that date.
     *
     * ---
     *
     * ### 2. **Convert busy times into ZonedDateTime**:
     *
     * ```kotlin
     * val busyRanges = busyList.map {
     *     ZonedDateTime.parse(it.start).withZoneSameInstant(zoneId) to ...
     * }
     * ```
     *
     * ➡️ All `busyList` times are converted from UTC to user’s local time zone for comparison.
     *
     * ---
     *
     * ### 3. **Create 30-minute slots throughout the day**:
     *
     * ```kotlin
     * while (current < endOfDay) {
     *     val slotEnd = current + 30 minutes
     * ```
     *
     * ➡️ Start from 12:00 AM and go forward in 30-minute chunks (or whatever interval you set).
     *
     * ---
     *
     * ### 4. **Check if current slot overlaps with any busy time**:
     *
     * ```kotlin
     * val isOverlapping = busyRanges.any { ... }
     * ```
     *
     * ➡️ If the current slot **touches any busy time**, it’s skipped.
     *
     * ---
     *
     * ### 5. **If not overlapping, add it as a free slot**:
     *
     * ```kotlin
     * slots.add(TimeSlot(...))
     * ```
     *
     * ➡️ If free, save:
     *
     * * `label` like "2:00 PM"
     * * `startTime`
     * * `endTime`
     * * `duration`
     *
     * ---
     *
     * ### 6. **Move to the next slot**:
     *
     * ```kotlin
     * current = slotEnd
     * ```
     *
     * ➡️ Repeat until full day is checked.
     *
     * ---
     *
     * ## 🧾 Output:
     *
     * Returns a list of all **free 30-min time blocks** that are not overlapping with busy times.
     *
     * ---
     *
     * ## 🎯 Example:
     *
     * If the person is busy from **2:00–2:30 PM** and **3:00–4:00 PM**,
     * the result will include slots like:
     *
     * * ✅ 1:30–2:00 PM
     * * ❌ 2:00–2:30 PM (busy)
     * * ✅ 2:30–3:00 PM
     * * ❌ 3:00–4:00 PM (busy)
     * * ✅ 4:00–4:30 PM
     * * ✅ 4:30–5:00 PM
     *   ... and so on
     *
     * ---
     *
     * Let me know if you want a diagram or visual for this logic too bro 👊
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
   private fun generateFreeSlotsForFullDaysSlot(
        date: MeetingDate,
        busyList: List<BusyTime>,
        timeZone: String,
        intervalMinutes: Long = 30
    ): List<TimeSlot> {
        val zoneId = ZoneId.of(timeZone)
        val formatter = DateTimeFormatter.ofPattern("h:mm a")

        val startOfDay = ZonedDateTime.of(
            date.yearNumber,
            date.monthNumber,
            date.dayNumber,
            0, 0, 0, 0,
            zoneId
        )
        val endOfDay = startOfDay.plusDays(1)

        val busyRanges = busyList.map {
            ZonedDateTime.parse(it.start).withZoneSameInstant(zoneId) to
                    ZonedDateTime.parse(it.end).withZoneSameInstant(zoneId)
        }

        val slots = mutableListOf<TimeSlot>()
        var current = startOfDay

        while (current < endOfDay) {
            val slotEnd = current.plusMinutes(intervalMinutes)

            val isOverlapping = busyRanges.any { (busyStart, busyEnd) ->
                current < busyEnd && slotEnd > busyStart
            }

            if (!isOverlapping) {
                val label = current.format(formatter)
                val duration = Duration.between(current, slotEnd)

                slots.add(
                    TimeSlot(
                        label = label,
                        startTime = current,
                        endTime = slotEnd,
                        duration = duration
                    )
                )
            }

            current = slotEnd
        }

        return slots
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateFreeSlotsForOfficeTime(
        date: MeetingDate,
        busyList: List<BusyTime>,
        timeZone: String,
        intervalMinutes: Long = 30
    ): List<TimeSlot> {
        val zoneId = ZoneId.of(timeZone)
        val formatter = DateTimeFormatter.ofPattern("h:mm a")

        // Set office hours: 9 AM to 5 PM
        val startOfOffice = ZonedDateTime.of(
            date.yearNumber,
            date.monthNumber,
            date.dayNumber,
            9, 0, 0, 0,
            zoneId
        )
        val endOfOffice = startOfOffice.withHour(17).withMinute(0)

        // Convert busy times to ZonedDateTime in local timezone
        val busyRanges = busyList.map {
            ZonedDateTime.parse(it.start).withZoneSameInstant(zoneId) to
                    ZonedDateTime.parse(it.end).withZoneSameInstant(zoneId)
        }

        val slots = mutableListOf<TimeSlot>()
        var current = startOfOffice

        while (current < endOfOffice) {
            val slotEnd = current.plusMinutes(intervalMinutes)

            // Make sure slot does not go beyond office end
            if (slotEnd > endOfOffice) break

            val isOverlapping = busyRanges.any { (busyStart, busyEnd) ->
                current < busyEnd && slotEnd > busyStart
            }

            if (!isOverlapping) {
                val label = current.format(formatter)
                val duration = Duration.between(current, slotEnd)

                slots.add(
                    TimeSlot(
                        label = label,
                        startTime = current,
                        endTime = slotEnd,
                        duration = duration
                    )
                )
            }

            current = slotEnd
        }

        return slots
    }


    /**
     * ### 🔹 `toFullDayUtcRange(timeZone: String): Pair<String, String>`
     *
     * This function takes a `MeetingDate` and a time zone,
     * and returns a **full-day time range in UTC** format:
     *
     * * `timeMin`: 12:00 AM (start of the day) in UTC
     * * `timeMax`: 12:00 AM (start of the next day) in UTC
     *
     * It ensures you cover the **entire day** (from midnight to midnight)
     * based on the user’s local time zone, but converted to **UTC**
     * for things like Google Calendar API.
     *
     * ✅ Example:
     * If your selected date is July 25, 2025, and your time zone is "Asia/Karachi":
     *
     * timeMin = "2025-07-24T19:00:00Z" (== 12 AM Karachi time on July 25)
     *
     * timeMax = "2025-07-25T19:00:00Z" (== 12 AM Karachi time on July 26)
     *
     * This means you're correctly covering all of July 25th in user’s local time — but sending it in UTC format as Google Calendar expects. 🕛✅
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
   private fun MeetingDate.toFullDaySlotTimeRange(timeZone: String): Pair<String, String> {
        val localDate = LocalDate.of(yearNumber, monthNumber, dayNumber)
        val zoneId = ZoneId.of(timeZone)

        val timeMin = localDate
            .atStartOfDay(zoneId)
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT)

        val timeMax = localDate
            .plusDays(1)
            .atStartOfDay(zoneId)
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT)

        return timeMin to timeMax
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun MeetingDate.toOfficeHoursRange(timeZone: String): Pair<String, String> {
        val localDate = LocalDate.of(yearNumber, monthNumber, dayNumber)
        val zoneId = ZoneId.of(timeZone)

        // 9:00 AM local time
        val officeStart = localDate
            .atTime(9, 0)
            .atZone(zoneId)
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT)

        // 5:00 PM local time
        val officeEnd = localDate
            .atTime(17, 0)
            .atZone(zoneId)
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT)

        return officeStart to officeEnd
    }

}