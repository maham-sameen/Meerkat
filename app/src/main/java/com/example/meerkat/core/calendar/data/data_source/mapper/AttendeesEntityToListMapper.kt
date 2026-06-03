package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.local.AttendeeEntity

class AttendeesEntityToListMapper : Mapper<List<AttendeeEntity>, List<String>> {
    override fun map(from: List<AttendeeEntity>): List<String> {
        return from.map { it.email }
    }
}