package com.example.meerkat.core.calendar.data.data_source.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar

@Entity(
    tableName = ConstantCalendar.ATTENDEE_ENTITY_TABLE ,
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class ,
            parentColumns = [ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY] ,
            childColumns = [ConstantCalendar.ATTENDEE_ENTITY_FOREIGN_KEY] ,
            onUpdate = ForeignKey.CASCADE ,
            onDelete = ForeignKey.CASCADE
        )
    ] ,
    indices = [Index(value = [ConstantCalendar.ATTENDEE_ENTITY_FOREIGN_KEY])]
)
data class AttendeeEntity(
    @PrimaryKey(autoGenerate = true)
    val atId : Long = 0 ,
    val email: String,
    val attendeeEventOwnerId : Long ,
    val attendeeId : String? = null ,

    @ColumnInfo(defaultValue = "false")
    val organizer: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    val self: Boolean = false ,
    val resource : Boolean? = null ,
    val optional : Boolean? = null,
    val responseStatus : String ,
    val comment : String? = null,

    @ColumnInfo(defaultValue = 0.toString())
    val additionalGuests : Int = 0,
)


// TODO comparison is base on properties except the primary key and foreign key as new event do not have it
fun AttendeeEntity.isContentEqual(other: AttendeeEntity): Boolean {
    return this.email == other.email &&
            this.attendeeId == other.attendeeId &&
            this.organizer == other.organizer &&
            this.self == other.self &&
            this.resource == other.resource &&
            this.optional == other.optional &&
            this.responseStatus == other.responseStatus &&
            this.comment == other.comment &&
            this.additionalGuests == other.additionalGuests
}

fun AttendeeEntity.isSameAs(other: AttendeeEntity): Boolean {
    return this.email == other.email
}
