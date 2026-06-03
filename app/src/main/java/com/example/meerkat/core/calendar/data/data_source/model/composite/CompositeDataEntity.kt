package com.example.meerkat.core.calendar.data.data_source.model.composite

import com.example.meerkat.core.calendar.data.data_source.model.local.AttendeeEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity

data  class CompositeDataEntity(
    val eventEntity: EventEntity,
    val attendees: List<AttendeeEntity>
)