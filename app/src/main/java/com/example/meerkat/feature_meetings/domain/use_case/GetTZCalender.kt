package com.example.meerkat.feature_meetings.domain.use_case

import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo

class GetTZCalender(
    private val repo: CalendarAvailabilityRepo
) {
    suspend operator fun invoke(): Result<String> {
        val result = repo.fetchUserTimeZone()
        return result.fold(
            onSuccess = { timeZoneInfo ->
                timeZoneInfo.let { Result.success(it) }
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }
}