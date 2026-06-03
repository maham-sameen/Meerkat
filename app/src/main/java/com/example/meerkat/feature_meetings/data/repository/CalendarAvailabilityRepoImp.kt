package com.example.meerkat.feature_meetings.data.repository

import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.local.AuthLocalDataSource
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyEventDto
import com.example.meerkat.core.calendar.data.data_source.utils.TokenResult
import com.example.meerkat.core.calendar.data.data_source.utils.handleApiResponse
import com.example.meerkat.core.calendar.data.data_source.utils.handleTokenFailure
import com.example.meerkat.core.calendar.data.data_source.utils.performActionWithFreshTokensSuspend
import com.example.meerkat.core.calendar.domain.repository.CalendarEntityRepo
import com.example.meerkat.feature_meetings.data.data_source.local.MeetingLocalDataSource
import com.example.meerkat.feature_meetings.data.data_source.model.remote.FreeBusyRequest
import com.example.meerkat.feature_meetings.data.data_source.remote.MeetingApi
import com.example.meerkat.feature_meetings.domain.model.BusyTime
import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import timber.log.Timber

class CalendarAvailabilityRepoImp(
    private val dataSource: AuthLocalDataSource ,
    private val api : MeetingApi ,
    private val calendarEntityRepo: CalendarEntityRepo ,
    private val meetingLocalDataSource: MeetingLocalDataSource ,
)  : CalendarAvailabilityRepo {


    override suspend fun fetchFreeBusyTimes(request: FreeBusyRequest): Result<List<BusyTime>> {
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchFreeBusyTimes() , request : ${request.toString()}")
        return handleApiResponse(
            dataSource = dataSource,
            call = {
                api.getFreeBusy(it, request)
            }
        ).fold(
            onSuccess = {val busyList = it.calendars.values.first().busy
                Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchFreeBusyTimes() , onSuccess() , busyList : ${busyList.toString()}")
                if (busyList != null) {
                    Result.success(busyList)
                } else {
                    Result.success(emptyList<BusyTime>())
                }},
            onFailure = {
                Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchFreeBusyTimes() , onFailure : ${it.toString()}")
                Result.failure(it)
            }
        )
    }

    override suspend fun fetchUserTimeZone(): Result<String> {
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchUserTimeZone()")
           return calendarEntityRepo.getCalendarInfo()
    }



    override suspend fun fetchHost(): Result<String> {
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchHost() ")
        val host = meetingLocalDataSource.getHost()
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchHost() , host : ${host.toString()}")
        return if (host.isNullOrEmpty()) {
            handleApiResponse(
                dataSource = dataSource,
                call = {
                    api.getUserInfo(it)
                }
            ).fold(
                onSuccess = {body ->
                    Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchHost() , onSuccess body : ${body.toString()}")
                    meetingLocalDataSource.saveHost(body.email)
                    Result.success(body.email)},
                onFailure = {
                    Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchHost() , onFailure : ${it.toString()}")
                    Result.failure(it)
                }
            )
        } else {
            Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , fetchHost()  we get the host from local data source")
            Result.success(host)
        }
    }

    override suspend fun insertEventRemote(event: MyEventDto): Result<MyEventDto> {
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , insertEventRemote() , event: ${event.toString()}")
        return handleApiResponse(
            dataSource = dataSource,
            call = { it ->
                Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , insertEventRemote() , call : ${it.toString()}")
                api.createEvent(
                    authHeader = it ,
                    1,
                    event
                )
            }
        )
    }

    override suspend fun deleteEventRemote(eventId : String) : Result<Unit>{
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , deleteEventRemote() , eventId : ${eventId.toString()}")
       val authState = dataSource.getAuthState()
        val authService = dataSource.authorizationService
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , deleteEventRemote() , authState : ${authState.toString()} ")
        return when (val tokenResult = authState.performActionWithFreshTokensSuspend(authService)){
            is TokenResult.Failure -> {
                Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , deleteEventRemote() token result is failure , ${tokenResult.exception.toString()}")
                val ex = tokenResult.exception
                return handleTokenFailure(ex , onSaveAuthState = {
                    dataSource.saveAuthState(authState)
                })
            }
            is TokenResult.Success -> {
                Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , deleteEventRemote() token result success , ${tokenResult.toString()}")
                val accessToken = tokenResult.accessToken
                val authHeader = "Bearer $accessToken"
                try {
                    val response = api.deleteEvent(
                        authHeader ,
                        eventId
                    )
                    if (response.isSuccessful) {
                        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , deleteEventRemote() response is success")
                        //For a DELETE request, especially to Google Calendar API, the response body is almost always empty
                        // (HTTP 204 No Content). So response.body() is usually null.
                        // no need to check for body is null
                        Result.success(Unit)
                    } else {
                        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , deleteEventRemote() response is failed  due to API error")
                        Result.failure(Exception("API Error: ${response.code()} - ${response.errorBody()?.string()}"))
                    }
                }catch (e : Exception){
                    Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , deleteEventRemote() exception in catch block ${e.message.toString()}")
                    if(e is CancellationException) throw  e
                    with(NonCancellable){ Result.failure(Exception(e.localizedMessage)) }
                }
            }
        }
    }

    override suspend fun updateEventRemote(
        eventId : String ,
        event: MyEventDto
    ): Result<MyEventDto> {
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , updateEventRemote()")
        return handleApiResponse(
            dataSource = dataSource,
            call = {
                    authHeader ->
                Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , updateEventRemote() , authHeader : ${authHeader.toString()}")
                api.updateEvent(
                    authHeader = authHeader,
                    eventId = eventId,
                    conferenceVersion = 1,
                    updatedEvent = event,
                )
            }
        )
    }

    override suspend fun getAnEventRemote(eventId: String): Result<MyEventDto> {
        Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , getAnEventRemote , eventId : $eventId")
        return handleApiResponse(
            dataSource = dataSource,
            call = {authHeader ->
                Timber.tag(Constants.appName).i("\uD83E\uDD6C [CalendarAvailabilityRepoImp] , getAnEventRemote , authHeader : ${authHeader.toString()}")
                api.getAnEvent(
                    authHeader = authHeader,
                    eventId = eventId
                )
            }
        )
    }
}
