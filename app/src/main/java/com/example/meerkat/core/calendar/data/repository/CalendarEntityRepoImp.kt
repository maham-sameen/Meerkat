package com.example.meerkat.core.calendar.data.repository

import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.local.AuthLocalDataSource
import com.example.meerkat.core.calendar.data.data_source.local.CalendarDao
import com.example.meerkat.core.calendar.data.data_source.mapper.CalendarInfoDtoToCalendarEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.CalendarEntity
import com.example.meerkat.core.calendar.data.data_source.remote.CalendarApi
import com.example.meerkat.core.calendar.data.data_source.utils.TokenResult
import com.example.meerkat.core.calendar.data.data_source.utils.handleTokenFailure
import com.example.meerkat.core.calendar.data.data_source.utils.performActionWithFreshTokensSuspend
import com.example.meerkat.core.calendar.domain.repository.CalendarEntityRepo
import com.example.meerkat.core.calendar.data.data_source.model.remote.CalendarInfoDto
import com.example.meerkat.core.calendar.data.data_source.utils.handleApiResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber

class CalendarEntityRepoImp(
    private val calendarDao: CalendarDao ,
    private val dataSource: AuthLocalDataSource ,
    private val api: CalendarApi ,
    private val calendarInfoDtoToCalendarEntity: CalendarInfoDtoToCalendarEntity
) : CalendarEntityRepo {
    override  fun getAllCalendarEntities(): Flow<List<CalendarEntity>> {
        Timber.tag(Constants.appName).i("\uD83C\uDF9F [CalendarEntityRepoImp] getAllCalendarEntities()")
        return calendarDao.getAllCalendars()
    }

    override suspend fun getCalendarInfo(): Result<String> {
        Timber.tag(Constants.appName).i("\uD83C\uDF9F [CalendarEntityRepoImp]  fetchUserTimeZone()")
        val allCalendars = getAllCalendarEntities().first()
        val timeZone: String? = allCalendars.firstOrNull()?.timeZone
        Timber.tag(Constants.appName).i("\uD83D\uDC0C [GetCalendarInfo] , timezone : ${timeZone.toString()} , \n  calendar : ${allCalendars.toString()} ")
        if (timeZone != null) {
            Timber.tag(Constants.appName).i("\uD83C\uDF9F [CalendarEntityRepoImp]  timeZone is not null ")
           return Result.success(timeZone)
        }
        return handleApiResponse(
            dataSource = dataSource,
            call = {
                api.getUserCalendarInfo(it)
            }
        ).fold(
            onSuccess = {
                Timber.tag(Constants.appName).i("\uD83C\uDF9F [CalendarEntityRepoImp] , onSuccess : ${it.toString()}")
                calendarDao.upsert(calendarInfoDtoToCalendarEntity.map(it))
                Result.success(it.timeZone ?:"UTC")
            },
            onFailure = {
                Timber.tag(Constants.appName).i("\uD83C\uDF9F [CalendarEntityRepoImp] , onFailure : ${it.toString()}")
                Result.failure(it)}
        )
    }
}

