package com.example.meerkat.core.calendar.data.data_source.model.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.meerkat.core.calendar.data.data_source.model.remote.BirthdayPropertiesDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.ExtendedPropertiesDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.FocusTimePropertiesDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyPersonDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyTimeDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.OutOfOfficePropertiesDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.SourceDto
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Entity(
    tableName = ConstantCalendar.EVENT_ENTITY_TABLE,
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val eventPrimaryId : Long = 0,
    val eventId: String,
    val kind: String,
    val etag: String,
    val status: String,
    val htmlLink: String,
    val created: String,
    val updated: String,
    val summary: String?,
    val description: String?,
    val location: String?,
    val colorId: String?,

    @Embedded(prefix = "creator_")
    val creator: MyPersonDto?,

    @Embedded(prefix = "organizer_")
    val organizer: MyPersonDto?,

    @Embedded(prefix = "start_")
    val start: MyTimeDto,

    @Embedded(prefix = "end_")
    val end: MyTimeDto,

    val iCalUID : String,
    val transparency : String? = null,
    val visibility: String? = null,
    @ColumnInfo(defaultValue = "0")
    val sequence : Int = 0,


    val attendeesOmitted : Boolean? = null,
    @Embedded("extendedProperties_")
    val extendedPropertiesDto : ExtendedPropertiesDto? = null,

    val hangoutLink : String? = null,

    val anyoneCanAddSelf : Boolean? = null, //If true, anyone with the link can add themselves to the event.



    @ColumnInfo(defaultValue = "true")
    val guestsCanInviteOthers : Boolean = true,

    @ColumnInfo(defaultValue = "false")
    val guestsCanModify : Boolean = false,
    @ColumnInfo(defaultValue = "true")
    val guestsCanSeeOtherGuests : Boolean = true,
    val privateCopy : Boolean? = null,

    @ColumnInfo(defaultValue = "false")
    val locked : Boolean = false,

    @Embedded("source_")
    val sourceDto: SourceDto,

    @Embedded(prefix = "outOfOfficeProperties_")
    val outOfOfficePropertiesDto : OutOfOfficePropertiesDto? = null,

    @Embedded(prefix = "focusTimeProperties_")
    val focusTimePropertiesDto : FocusTimePropertiesDto? = null,

    @Embedded
    val birthdayPropertiesDto : BirthdayPropertiesDto? = null,
    val eventType : String,


    @ColumnInfo(name = "recurrence_list")
    val recurrence: List<String>? // ✅ This works with the converter now
)

@RequiresApi(Build.VERSION_CODES.O)
fun EventEntity.toTimeSlot(): TimeSlot {
    val zoneId = ZoneId.of(start.timeZone)

    // Parse start and end
    val startZdt = ZonedDateTime.parse(start.dateTime).withZoneSameInstant(zoneId)
    val endZdt = ZonedDateTime.parse(end.dateTime).withZoneSameInstant(zoneId)

    val duration = Duration.between(startZdt, endZdt)

    val formatter = DateTimeFormatter.ofPattern("h:mm a") // e.g., 9:00 AM
    val label = startZdt.format(formatter)

    return TimeSlot(
        label = label,
        startTime = startZdt,
        endTime = endZdt,
        duration = duration ,
        isAvailable = true
    )
}