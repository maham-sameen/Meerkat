package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.local.EntryPointEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.EntryPointDto

class EntryPointDtoToEntryPointEntityMapper : Mapper<Pair<EntryPointDto, Long>, EntryPointEntity> {
    override fun map(from: Pair<EntryPointDto, Long>): EntryPointEntity {
        val (entryPoint, ownerId) = from
        return EntryPointEntity(
            conferenceDataOwnerId = ownerId,
            entryPointType = entryPoint.entryPointType,
            uri = entryPoint.uri,
            label = entryPoint.label,
            pin = entryPoint.pin,
            accessCode = entryPoint.accessCode,
            meetingCode = entryPoint.meetingCode,
            passcode = entryPoint.passcode,
            password = entryPoint.password
        )
    }
}