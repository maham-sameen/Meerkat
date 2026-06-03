package com.example.meerkat.core.calendar.data.data_source.model.remote

data class MyCalendarDto(
    val kind : String,
    val etag : String,
    val summary : String,
    val description : String,
    val updated: String,
    val timeZone: String,
    val accessRole : String,
    val defaultReminders : List<OverridesDto>,
    val nextSyncToken : String?,
    val items : List<MyEventDto> ,
    val nextPageToken: String? = null, // ← ADD THIS
)

data class MyEventDto(
    val kind : String,
    val etag : String?,
    val id : String?,
    val status : String?,
    val htmlLink : String?,
    val created : String?,
    val updated : String?,
    val summary : String?,
    val description : String?,
    val location : String?,
    val colorId : String?  = null,
    val creator : MyPersonDto? = null,
    val organizer : MyPersonDto? = null,
    val start : MyTimeDto,
    val end : MyTimeDto,
    val endTimeUnspecified : Boolean? = null,
    val recurringEventId : String?,
    val originalStartTime : MyTimeDto?,



    val recurrence : List<String>?,


    val iCalUID : String? = null,
    val transparency : String? = null,
    val visibility: String? = null,
    val sequence : Int? = null,


    val attendees : List<AttendeeDto>? = null,



    val attendeesOmitted : Boolean? = null,
    val extendedProperties : ExtendedPropertiesDto? = null,
    val hangoutLink : String? = null,



    val conferenceData: ConferenceDataDto? = null,
    // this contain all remainder means default and new one .


    val anyoneCanAddSelf : Boolean? = null, //If true, anyone with the link can add themselves to the event.
    val guestsCanInviteOthers : Boolean? = null,
    val guestsCanModify : Boolean? = null,
    val guestsCanSeeOtherGuests : Boolean? = null,
    val privateCopy : Boolean? = null,
    val locked : Boolean? = null,
    val reminders : RemindersDto? = null,
    val source: SourceDto?  = null,
    val outOfOfficeProperties : OutOfOfficePropertiesDto? = null,
    val focusTimeProperties : FocusTimePropertiesDto? = null,


    val attachments : List<AttachmentsDto>? = null,


    val birthdayProperties : BirthdayPropertiesDto? = null,
    val eventType : String?
)

data class BirthdayPropertiesDto (
    val contact : String? = null ,
    val type : String? = null ,
    val customTypeName : String? = null
)


data class AttachmentsDto(
    val fileUrl : String ,
    val title : String ,
    val mimeType : String ,
    val iconLink : String ,
    val fileId : String
)
data class FocusTimePropertiesDto(
    val autoDeclineMode : String ,
    val declineMessage: String ,
    val chatStatus: String
)
data class OutOfOfficePropertiesDto(
    val autoDeclineMode : String ,
    val declineMessage : String
)
data class SourceDto(
    val url : String ,
    val title : String
)
data class RemindersDto(
    val useDefault : Boolean ,
    val overrides : List<OverridesDto>?
)

data class OverridesDto(
    val method : String ,
    val minutes : Int
)

data class MyPersonDto (
    val id : String? = null ,
    val email : String? ,
    val self : Boolean?
)

data class MyTimeDto(
    val date : String? = null,
    val dateTime : String ,
    val timeZone : String
)

data class AttendeeDto(
    val id : String? = null ,
    val email: String,
    val organizer: Boolean = false,
    val self: Boolean = false ,
    val resource : Boolean? = null ,
    val optional : Boolean? = null,
    val responseStatus : String? = null,
    val comment : String? = null,
    val additionalGuests : Int = 0,
)

data class ExtendedPropertiesDto(
    val private: Map<String, String>? = null,
    val shared: Map<String, String>? = null
)

data class ConferenceDataDto(
    val createRequest: CreateRequestDto?,
    val entryPoints: List<EntryPointDto>? = null,
    val conferenceSolution: ConferenceSolutionDto? = null,
    val conferenceId: String? = null,
    val signature: String? = null,
    val notes: String? = null
)
data class CreateRequestDto(
    val requestId : String,
    val conferenceSolutionKey: ConferenceSolutionKeyDto,
    val status: StatusDto
)

data class ConferenceSolutionKeyDto(
    val type : String
)


data class StatusDto (
    val statusCode : String
)

data class EntryPointDto(
    val entryPointType : String ,
    val uri : String? = null ,
    val label : String ,
    val pin : String? = null ,
    val accessCode : String? = null ,
    val meetingCode : String? = null ,
    val passcode : String? = null ,
    val password : String? = null
)


data class ConferenceSolutionDto(
    val key: ConferenceSolutionKeyDto,
    val name: String,
    val iconUri: String?
)

