package com.example.meerkat.core.calendar.domain.use_case

import com.example.meerkat.core.calendar.domain.util.MeerkatEventLinks
import com.example.meerkat.core.calendar.domain.util.MeerkatJoinLink

class GetMeerkatEventLinks {

    fun generateJoinLinkPair(eventId: String, meetUrl: String): MeerkatJoinLink {
        println("\uD83D\uDC39 [GetMeerkatEventLinks] generateJoinLinkPair  , eventId : $eventId , meetUrl : $meetUrl")
        return MeerkatJoinLink(
            meerkatJoinLink = "https://meerkat.com/events/$eventId/join",
            originalGoogleMeetLink = meetUrl
        )
    }

    operator fun invoke(eventId: String , meetUrl: String): MeerkatEventLinks {
        println("\uD83D\uDC39 [GetMeerkatEventLinks]   , eventId : $eventId , meetUrl : $meetUrl")
        val base = "https://meerkat.com/events"
        return MeerkatEventLinks(
            joinLink = generateJoinLinkPair(eventId, meetUrl),
            cancelLink = "$base/$eventId/cancel",
            rescheduleLink = "$base/$eventId/reschedule"
        )
    }

}