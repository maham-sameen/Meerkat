package com.example.meerkat.feature_notification.data.data_source.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.core.calendar.data.data_source.mapper.Mapper
import com.example.meerkat.core.calendar.domain.model.EventData
import com.example.meerkat.feature_notification.domain.model.NotificationEvent
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.Duration
import java.util.Locale
import java.time.OffsetDateTime
import java.util.TimeZone


@RequiresApi(Build.VERSION_CODES.O)
class EventDataToNotificationEventMapper : Mapper<EventData, NotificationEvent> {
    override fun map(from: EventData): NotificationEvent {
        println("eventData : $from")
        val parsed = OffsetDateTime.parse(from.startDateTime)
        val attendees = from.attendees.map {it ->
                extractNameFromEmail(it)
            }

        return NotificationEvent(
            id = from.id,
            organizer = if(from.organizerName != null) extractNameFromEmail(from.organizerName) else null,
            title = from.title,
            timeAgo = getDurationAgo(from.startDateTime),
            meetingTitle = from.title,
            meetingWith = attendees,
            meetingDateTime = getAmOrPmTime(parsed) ,
            timeZoneName = getTimeZoneName(from.timeZone , parsed)
        )
    }

    private fun getAmOrPmTime(parsed  :  OffsetDateTime): String{
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        return parsed.format(formatter)
    }

    private fun getTimeZoneName(timeZone : String , parsed :  OffsetDateTime): String{
        val zoneId = ZoneId.of(timeZone)
        val timeZoneObj = TimeZone.getTimeZone(zoneId)
        return timeZoneObj.displayName // e.g., "Pakistan Standard Time"
    }

    private fun getDurationAgo(startTime: String): String {
        val eventTime = ZonedDateTime.parse(startTime)
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val duration = Duration.between(now, eventTime) // now -> eventTime

        val isFuture = duration.seconds > 0

        val absDuration = duration.abs()

        val timeString = when {
            absDuration.toDays() >= 7 -> "${absDuration.toDays() / 7}w"
            absDuration.toDays() >= 1 -> "${absDuration.toDays()}d"
            absDuration.toHours() >= 1 -> "${absDuration.toHours()}h"
            else -> "${absDuration.toMinutes()}m"
        }

        return if (isFuture) "in $timeString" else "$timeString ago"
    }


    private  fun extractNameFromEmail(email: String): String {
        val username = email.substringBefore("@")
        val name = username
            .replace(Regex("[^a-zA-Z]"), " ") // Remove digits and symbols
            .split(" ")                       // Split on spaces
            .filter { it.isNotBlank() }       // Remove empty parts
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } } // Capitalize
        return name.trim()
    }
}
