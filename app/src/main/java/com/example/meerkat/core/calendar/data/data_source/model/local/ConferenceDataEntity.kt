package com.example.meerkat.core.calendar.data.data_source.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar


data class MyCreateRequest(
     val requestId: String,
     val conferenceSolutionKey: String,
     val status: String
)


data class MyConferenceSolution(
     val key: String,
     val name: String,
     val iconUri: String?
)

@Entity(
    tableName = ConstantCalendar.CONFERENCE_DATA_ENTITY_TABLE ,
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = [ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY],
            childColumns = [ConstantCalendar.CONFERENCE_DATA_ENTITY_TABLE_FOREIGN_KEY],
            onDelete = ForeignKey.CASCADE ,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = [ConstantCalendar.CONFERENCE_DATA_ENTITY_TABLE_FOREIGN_KEY])]
)
data class ConferenceDataEntity(
    @PrimaryKey(autoGenerate = true)
    val cdeId : Long = 0 ,

    val eventOwnerId: Long, // FK to EventEntity.eventId
    @Embedded(prefix = "createRequest_")
    val createRequest: MyCreateRequest?,
    @Embedded(prefix = "conferenceSolution_")
    val conferenceSolution: MyConferenceSolution?,
    val conferenceId: String? = null,
    val signature: String? = null,
    val notes: String? = null
)



@Entity(
    tableName = ConstantCalendar.ENTRY_POINT_ENTITY_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = ConferenceDataEntity::class,
            parentColumns = [ConstantCalendar.CONFERENCE_DATA_ENTITY_TABLE_PRIMARY_KEY],
            childColumns = [ConstantCalendar.ENTRY_POINT_ENTITY_TABLE_FOREIGN_KEY],
            onDelete = ForeignKey.CASCADE ,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = [ConstantCalendar.ENTRY_POINT_ENTITY_TABLE_FOREIGN_KEY])]
)
data class EntryPointEntity(
    @PrimaryKey(autoGenerate = true)
    val entryPointId: Long = 0,

    val conferenceDataOwnerId: Long, // FK to ConferenceDataEntity

    val entryPointType: String,
    val uri : String? = null ,
    val label : String? = null ,
    val pin : String? = null ,
    val accessCode : String? = null ,
    val meetingCode : String? = null ,
    val passcode : String? = null ,
    val password : String? = null
)



data class ConferenceDataWithEntryPoints(
    @Embedded val conferenceData: ConferenceDataEntity,

    @Relation(
        parentColumn = ConstantCalendar.CONFERENCE_DATA_ENTITY_TABLE_PRIMARY_KEY,
        entityColumn = ConstantCalendar.ENTRY_POINT_ENTITY_TABLE_FOREIGN_KEY
    )
    val entryPoints: List<EntryPointEntity> = emptyList<EntryPointEntity>()
)
