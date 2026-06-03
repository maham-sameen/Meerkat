//package com.example.meerkat.core.calendar.data.data_source.paging_source
//
//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.LoadType
//import androidx.paging.PagingState
//import androidx.paging.RemoteMediator
//import com.example.meerkat.core.calendar.data.data_source.local.CalendarDao
//import com.example.meerkat.core.calendar.data.data_source.local.RemoteKeyDao
//import com.example.meerkat.core.calendar.data.data_source.model.local.EventWithAttendees
//import com.example.meerkat.core.calendar.data.data_source.model.local.RemoteKey
//import com.example.meerkat.core.calendar.data.data_source.remote.CalendarRemoteDataSource
//import kotlinx.coroutines.flow.first
//
//@OptIn(ExperimentalPagingApi::class)
//class EventRemoteMediator(
//    private val remoteKeyDao: RemoteKeyDao,
//    private val calendarDao: CalendarDao,
//    private val calendarRemoteDataSource: CalendarRemoteDataSource,
//    private val timeMin: String,
//    private val timeMax: String,
//    private val query: String?,
//    private val maxResults: Int,
//    private val updateEntitiesOfDB: UpdateEntitiesOfDB
//) : RemoteMediator<Int, EventWithAttendees>() {
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, EventWithAttendees>
//    ): MediatorResult {
//        return try {
//            var page: Int
//            var pageToken: String?
//            println("<-------------------------load()---------------------------->")
//            when (loadType) {
//                LoadType.REFRESH -> {
//                    println("LoadType is refresh")
//                    calendarDao.clearAll()
//                    remoteKeyDao.clearAll()
//                    page = 1
//                    pageToken = null
//                }
//
//                LoadType.PREPEND -> {
//                    println("LoadType is PrePend")
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//
//                LoadType.APPEND -> {
//                    println("LoadType is Append")
//                    val remoteKey = state.pages
//                        .lastOrNull { it.data.isNotEmpty() }
//                        ?.data?.firstOrNull()
//                        ?.let { entity ->
//                            println("Entity ${entity.toString()}")
//                            remoteKeyDao.getRemoteKey(entity.event.eventPrimaryId)
//                        }
//
//                    if (remoteKey?.nextPageKey == null) {
//                        println("remoteKey nextPageKey is null")
//                        return MediatorResult.Success(endOfPaginationReached = true)
//                    }
//                    println("otherwise nextPageKey ${remoteKey.nextPageKey} , nextPageToken ${remoteKey.nextPageToken} ")
//                    page = remoteKey.nextPageKey
//                    pageToken = remoteKey.nextPageToken
//                }
//            }
//
//            val response = calendarRemoteDataSource.makeApiCallWithRetrofit(
//                nextPageToken = pageToken,
//                timeMin = timeMin.takeIf { it.isNotBlank() },
//                timeMax = timeMax.takeIf { it.isNotBlank() },
//                q = query.takeIf { it!= null && it.isNotBlank() },
//                maxResults = maxResults
//            )
//
//            println("<------response in load()------>")
//
//            if (!response.isSuccess) {
//                println("response is failure")
//                val error = response.exceptionOrNull() ?: Exception("Unknown API failure")
//                return MediatorResult.Error(error)
//            }
//            val calendarDto = response.getOrNull()
//                ?: return MediatorResult.Error(response.exceptionOrNull() ?: Exception("Empty response body"))
//
//            println("getCalendarDao ${calendarDto.toString()}")
//
//           val calendarId =  updateEntitiesOfDB.invoke(calendarDto)
//
//            val nextPageToken = calendarDto.nextPageToken
//            val endOfPaginationReached = nextPageToken == null
//            val nextKey = if (endOfPaginationReached) null else page + 1
//
//            println("nextPageToken : ${nextPageToken.toString()} , endOfPaginationReached ${endOfPaginationReached.toString()}  , nextKey : ${nextKey.toString()}")
//
//            val eventEntities = calendarDao.getEventListByCalendarId(calendarId)
//            eventEntities.first()?.events?.forEach{ event ->
//                remoteKeyDao.insert(
//                    RemoteKey(
//                        id = event.eventPrimaryId,
//                        nextPageKey = nextKey,
//                        nextPageToken = nextPageToken,
//                        query = query.orEmpty()
//                    )
//                )
//            }
//            MediatorResult.Success(endOfPaginationReached)
//        } catch (e: Exception) {
//            println("DB error: ${e}")
//            e.printStackTrace()
//            MediatorResult.Error(e)
//        }
//    }
//}