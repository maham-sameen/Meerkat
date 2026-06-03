package com.example.meerkat.feature_meetings.domain.model

import com.example.meerkat.core.calendar.domain.util.MeerkatJoinLink

data class Message(
    val meetingDescription : String = "Invitation: Maham and Freak Witch Meeting",
    val meerkatJoinLink: MeerkatJoinLink = MeerkatJoinLink(
        originalGoogleMeetLink = "https://meet.google.com/brh-cvfm-svv",
        meerkatJoinLink = "https://meerkat.com",
    ),
    val  time: String = "Thursday 21 Aug 2025 · 10:30am – 11am (Pakistan Standard Time)",
    val host: Person = Person("Maham" ,  "mahamsameenhere@gmail.com"),
    val attendee: List<Person> = listOf(Person("Freak" , "freakwitch@gmail.com")),
    val htmlLink: String = "https://calendar.app.google/TMSz6gYiK3fs6mxS8"
)


