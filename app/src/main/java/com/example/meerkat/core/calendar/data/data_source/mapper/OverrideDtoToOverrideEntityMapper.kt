package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.local.OverrideEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.OverridesDto

class OverrideDtoToOverrideEntityMapper : Mapper<Pair<OverridesDto, Long>, OverrideEntity> {
    override fun map(from: Pair<OverridesDto, Long>): OverrideEntity {
        val (override, ownerId) = from
        return OverrideEntity(
            method = override.method,
            minutes = override.minutes,
            reminderOwnerId = ownerId
        )
    }
}