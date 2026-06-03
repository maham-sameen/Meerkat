package com.example.meerkat.core.calendar.data.data_source.remote

import com.example.meerkat.core.calendar.data.data_source.model.remote.MyCalendarDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.MyEventDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.CalendarInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarApi {


    /**
     * | Parameter    | Description                                                            | Required?     | Use Case Example                          |
     * | ------------ | ---------------------------------------------------------------------- | ------------- | ----------------------------------------- |
     * | `timeMin`    | Only return events **after** this datetime.                            | ✅ Recommended | Fetch upcoming events (e.g. from now)     |
     * | `timeMax`    | Only return events **before** this datetime.                           | ✅ Recommended | Limit to a range (e.g. next 30 days)      |
     * | `maxResults` | Limit number of events returned in one call.                           | ✅ Recommended | Pagination (e.g. show 10 or 20 at a time) |
     * | `pageToken`  | Token to fetch the **next page** of results.                           | Optional      | Use when you get a `nextPageToken`        |
     * | `q`          | Text **search keyword** (searches title, location, description, etc.). | Optional      | Filter by keyword like "Maham"            |
     * | `syncToken`  | Token to fetch only **changed/deleted** events since last sync.        | Optional      | Ideal for syncing after first full load   |
     * | `updatedMin` | Only return events **updated after** this datetime.                    | Optional      | Good for lightweight manual updates/sync  |
     *
     */


    @GET("calendar/v3/calendars/primary/events")
    suspend fun getPrimaryCalendarEvents(
        @Header("Authorization") authHeader: String,
        @Query("timeMin") timeMin: String? = null,
        @Query("timeMax") timeMax: String? = null,
        @Query("maxResults") maxResults: Int? = null,
        @Query("pageToken") pageToken: String? = null,
        @Query("q") query: String? = null,
        @Query("syncToken") syncToken: String? = null,
        @Query("updatedMin") updatedMin: String? = null
    ): Response<MyCalendarDto>


    @GET("calendar/v3/calendars/primary")
    suspend fun getUserCalendarInfo(
        @Header("Authorization") authHeader: String
    ): Response<CalendarInfoDto>

}