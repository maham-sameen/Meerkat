package com.example.meerkat.core.calendar.data.data_source.utils

import com.example.meerkat.feature_contacts.data.data_source.util.ConstantContact

object ConstantCalendar {

    val SCOPE_PROFILE = "profile"
    val SCOPE_EMAIL = "email"
    val SCOPE_OPENID = "openid"

    val CLIENT_ID = "65613581288-0i1j4brksce10q7vsmlbn0jooqabucoa.apps.googleusercontent.com"
    val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
    val MESSAGE_DIGEST_ALGORITHM = "SHA-256"

    val URL_AUTHORIZATION = "https://accounts.google.com/o/oauth2/v2/auth"
    val URL_TOKEN_EXCHANGE = "https://www.googleapis.com/oauth2/v4/token"
    val URL_AUTH_REDIRECT = "com.example.meerkat:/oauth2redirect"
    val URL_LOGOUT = "https://accounts.google.com/o/oauth2/revoke?token="

    val URL_LOGOUT_REDIRECT = "com.example.meerkat:/logout"

    val SCOPE_CALENDAR = "https://www.googleapis.com/auth/calendar"


    val AUTH_PREF = "auth_prefs"

    val AUTH_STATE = "auth_state_key"
    val CURRENT_USER_ID  = "current_user_id"




    const val  CALENDAR_ENTITY_TABLE = "calendar_table"
    const val CALENDAR_ENTITY_TABLE_PRIMARY_KEY = "calendarId"
    const val CALENDAR_ENTITY_SUMMARY = "summary"

    const val OVERRIDE_ENTITY_TABLE_ = "override_table"
    const val OVERRIDE_ENTITY_FOREIGN_KEY_REMINDER = "reminderOwnerId"
    const val OVERRIDE_ENTITY_PRIMARY_KEY = "overrideId"

    const val EVENT_ENTITY_TABLE = "event_table"
    const val EVENT_ENTITY_PRIMARY_KEY = "eventPrimaryId"
    const val EVENT_ENTITY_iCalUID  = "iCalUID"



    const val ATTENDEE_ENTITY_TABLE = "attendee_table"
    const val ATTENDEE_ENTITY_FOREIGN_KEY = "attendeeEventOwnerId"
    const val ATTENDEE_ENTITY_PRIMARY_KEY = "atId"


    const val CONFERENCE_DATA_ENTITY_TABLE = "conferenceData_table"
    const val CONFERENCE_DATA_ENTITY_TABLE_PRIMARY_KEY = "cdeId"
    const val CONFERENCE_DATA_ENTITY_TABLE_FOREIGN_KEY = "eventOwnerId"

    const val ENTRY_POINT_ENTITY_TABLE = "entryPoint_table"
    const val ENTRY_POINT_ENTITY_TABLE_FOREIGN_KEY = "conferenceDataOwnerId"
    const val ENTRY_POINT_ENTITY_TABLE_PRIMARY_KEY = "entryPointId"

    const val ATTACHMENTS_ENTITY_TABLE = "attachment_table"
    const val ATTACHMENT_ENTITY_TABLE_FOREIGN_KEY = "attachmentOwnerId"
    const val ATTACHMENT_ENTITY_FILE_ID = "fileId"



    const val REMINDER_ENTITY_TABLE = "reminder_table"
    const val REMINDER_ENTITY_PRIMARY_KEY = "reminderId"
    const val REMINDER_ENTITY_FOREIGN_KEY = "reminderOwnerId"

    const val DATABASE_NAME = "happy_app_db"



    val constantContact = ConstantContact
}