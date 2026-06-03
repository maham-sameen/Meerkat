package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.composite.CompositeDataEntity
import com.example.meerkat.core.calendar.domain.model.EventData

class EntityToMyEventDataMapper(
    private val eventMapper: EventEntityToPartialEventDataMapper,
    private val attendeesMapper: AttendeesEntityToListMapper
) : Mapper<CompositeDataEntity, EventData> {

    override fun map(from: CompositeDataEntity): EventData {
        val partial = eventMapper.map(from.eventEntity)
        val attendees = attendeesMapper.map(from.attendees)

        return EventData(
            id = from.eventEntity.eventPrimaryId,
            title = partial.title,
            description = partial.description,
            startDateTime = partial.startDateTime,
            endDateTime = partial.endDateTime,
            timeZone = partial.timeZone,
            link = partial.link,
            location = partial.location,
            platform = partial.platform,
            status = partial.status,
            organizerName = partial.organizerName,
            attendees = attendees
        )
    }
}