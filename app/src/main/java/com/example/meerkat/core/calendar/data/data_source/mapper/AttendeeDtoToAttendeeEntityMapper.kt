package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.local.AttendeeEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.AttendeeDto

class AttendeeDtoToAttendeeEntityMapper : Mapper<Pair<AttendeeDto, Long>, AttendeeEntity> {
    override fun map(from: Pair<AttendeeDto, Long>): AttendeeEntity {
        return AttendeeEntity(
            email = from.first.email,
            attendeeEventOwnerId = from.second,
            attendeeId = from.first.id,
            organizer = from.first.organizer,
            self = from.first.self,
            resource = from.first.resource,
            optional = from.first.optional,
            responseStatus = from.first.responseStatus ?: "",
            comment = from.first.comment,
            additionalGuests = from.first.additionalGuests
        )
    }
}