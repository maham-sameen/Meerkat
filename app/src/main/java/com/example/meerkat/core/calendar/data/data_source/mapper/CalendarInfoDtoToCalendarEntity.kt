package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.local.CalendarEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.CalendarInfoDto

class CalendarInfoDtoToCalendarEntity : Mapper<CalendarInfoDto , CalendarEntity> {
    override fun map(from: CalendarInfoDto): CalendarEntity {
        return CalendarEntity(
            kind = from.kind,
            etag = from.etag,
            summary = from.summary,
            description = from.description,
            timeZone = from.timeZone,
            location = from.location,
            id = from.id,
        )
    }
}