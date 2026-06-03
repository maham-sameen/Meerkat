package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.mapper.MyEventDataPartial
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity

class EventEntityToPartialEventDataMapper : Mapper<EventEntity, MyEventDataPartial> {
    override fun map(from: EventEntity): MyEventDataPartial {
        return MyEventDataPartial(
            id = from.eventPrimaryId,
            title = from.summary ?: "",
            description = from.description,
            startDateTime = from.start.dateTime,
            endDateTime = from.end.dateTime,
            timeZone = from.start.timeZone,
            link = from.hangoutLink,
            location = from.location,
            platform = if (!from.hangoutLink.isNullOrBlank()) "Google Meet" else null,
            status = from.status,
            organizerName = from.organizer?.email
        )
    }
}