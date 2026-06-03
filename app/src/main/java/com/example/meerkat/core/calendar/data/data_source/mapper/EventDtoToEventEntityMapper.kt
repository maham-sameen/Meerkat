package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyEventDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.SourceDto

class EventDtoToEventEntityMapper : Mapper<Pair<MyEventDto, SourceDto>, EventEntity> {
    override fun map(from: Pair<MyEventDto, SourceDto>): EventEntity {
        val (event, source) = from
        return EventEntity(
            eventId = event.id!!,
            kind = event.kind,
            etag = event.etag!!,
            status = event.status ?: "confirmed",
            htmlLink = event.htmlLink!!,
            created = event.created!!,
            updated = event.updated!!,
            summary = event.summary,
            description = event.description,
            location = event.location,
            colorId = event.colorId,
            creator = event.creator,
            organizer = event.organizer,
            start = event.start,
            end = event.end,
            iCalUID = event.iCalUID!!,
            transparency = event.transparency,
            visibility = event.visibility,
            sequence = event.sequence!!,
            attendeesOmitted = event.attendeesOmitted,
            extendedPropertiesDto = event.extendedProperties,
            hangoutLink = event.hangoutLink,
            anyoneCanAddSelf = event.anyoneCanAddSelf,
            guestsCanInviteOthers = event.guestsCanInviteOthers == true,
            guestsCanModify = event.guestsCanModify == true,
            guestsCanSeeOtherGuests = event.guestsCanSeeOtherGuests == true,
            privateCopy = event.privateCopy,
            locked = event.locked == true,
            sourceDto = source,
            outOfOfficePropertiesDto = event.outOfOfficeProperties,
            focusTimePropertiesDto = event.focusTimeProperties,
            birthdayPropertiesDto = event.birthdayProperties,
            eventType = event.eventType ?: "default",
            recurrence = event.recurrence
        )
    }
}