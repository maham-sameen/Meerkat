package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.local.ConferenceDataEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.MyConferenceSolution
import com.example.meerkat.core.calendar.data.data_source.model.local.MyCreateRequest
import com.example.meerkat.core.calendar.data.data_source.model.remote.ConferenceDataDto

class ConferenceDataDtoToConferenceDataEntityMapper :
    Mapper<Pair<ConferenceDataDto, Long>, ConferenceDataEntity> {
    override fun map(from: Pair<ConferenceDataDto, Long>): ConferenceDataEntity {
        val (conferenceData, ownerId) = from
        return ConferenceDataEntity(
            eventOwnerId = ownerId,
            createRequest = conferenceData.createRequest?.let {
                MyCreateRequest(
                    requestId = it.requestId,
                    conferenceSolutionKey = it.conferenceSolutionKey.type,
                    status = it.status.statusCode
                )
            },
            conferenceSolution = conferenceData.conferenceSolution?.let {
                MyConferenceSolution(
                    key = it.key.type,
                    name = it.name,
                    iconUri = it.iconUri
                )
            },
            conferenceId = conferenceData.conferenceId,
            signature = conferenceData.signature,
            notes = conferenceData.notes
        )
    }
}