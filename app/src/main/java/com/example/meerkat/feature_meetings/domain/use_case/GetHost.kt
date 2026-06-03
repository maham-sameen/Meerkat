package com.example.meerkat.feature_meetings.domain.use_case

import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee

class GetHost(
    private val calendarAvailabilityRepo: CalendarAvailabilityRepo ,
    private val  extractNameFromEmail: ExtractNameFromEmail
) {

    suspend operator fun invoke(): Result<MeetingAttendee> {
        val hostResult = calendarAvailabilityRepo.fetchHost()

        return hostResult.fold(
            onSuccess = { host ->
                Result.success(
                    MeetingAttendee(
                        profileText = extractProfileTextFromEmail(host),
                        name = extractNameFromEmail(host),
                        isHost = true
                    )
                )
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }

    private fun extractProfileTextFromEmail(email: String): String{
        val username = email.substringBefore("@")
        val parts = username
            .replace(Regex("[^a-zA-Z]"), " ") // Keep only letters, replace digits/symbols with space
            .split(" ")
            .filter { it.isNotBlank() }

        val initials = parts
            .map { it.first().uppercaseChar() }
            .joinToString("")
        return initials
    }


}