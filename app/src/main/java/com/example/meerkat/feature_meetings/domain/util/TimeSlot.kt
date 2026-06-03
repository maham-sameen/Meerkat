package com.example.meerkat.feature_meetings.domain.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.feature_meetings.domain.use_case.meeting_days.GetMeetingDates
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.ZonedDateTime

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class TimeSlot(
    val label: String, // e.g. "9:00 AM"
    @Serializable(with = ZonedDateTimeSerializer::class)
    val startTime: ZonedDateTime,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val endTime: ZonedDateTime,
    @Serializable(with = DurationSerializer::class)
    val duration: Duration,
    val isAvailable: Boolean = true
)

@RequiresApi(Build.VERSION_CODES.O)
fun MutableList<TimeSlot>.insertSorted(newSlot: TimeSlot) {
    val index = indexOfFirst { it.startTime.isAfter(newSlot.startTime) }
    if (index == -1) {
        // If no later element found, add at the end
        add(newSlot)
    } else {
        // Insert before the first element that starts after newSlot
        add(index, newSlot)
    }
}


object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): ZonedDateTime {
        return ZonedDateTime.parse(decoder.decodeString())
    }
}


object DurationSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Duration", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeString(value.toString()) // ISO-8601 format (e.g. PT1H30M)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): Duration {
        return Duration.parse(decoder.decodeString())
    }
}