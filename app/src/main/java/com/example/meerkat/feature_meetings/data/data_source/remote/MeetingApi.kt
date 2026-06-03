package com.example.meerkat.feature_meetings.data.data_source.remote

import com.example.meerkat.core.calendar.data.data_source.model.remote.MyEventDto
import com.example.meerkat.core.calendar.data.data_source.model.remote.CalendarInfoDto
import com.example.meerkat.feature_meetings.data.data_source.model.remote.FreeBusyRequest
import com.example.meerkat.feature_meetings.data.data_source.model.remote.FreeBusyResponse
import com.example.meerkat.feature_meetings.data.data_source.model.remote.UserInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface MeetingApi {

    /**
     * Alright bro — let’s break this down so you **fully** get `@POST`, `@Header`, and `@Body` in Retrofit, not just what they “do,” but why they exist and how they work together.
     *
     * ---
     *
     * ## 1️⃣ `@POST`
     *
     * * **What it means:**
     *   This tells Retrofit you want to make an **HTTP POST request**.
     * * **Why POST?**
     *   In HTTP, there are different methods (GET, POST, PUT, DELETE, etc.), each with a purpose:
     *
     *   * **GET** → request data from a server (read-only).
     *   * **POST** → send data **to** the server to create/perform something.
     *   * **PUT** → send data to **replace** something.
     *   * **DELETE** → remove something.
     * * In your case:
     *
     *   ```kotlin
     *   @POST("calendar/v3/freeBusy")
     *   ```
     *
     *   Means:
     *
     *   * Retrofit will send a POST request to:
     *     **`https://www.googleapis.com/calendar/v3/freeBusy`**
     *     (`BASE_URL` + endpoint from `@POST`)
     *
     * ---
     *
     * ## 2️⃣ `@Header("Authorization")`
     *
     * * **What it means:**
     *   This adds an HTTP **header** named `"Authorization"` to your request.
     * * **Why headers?**
     *   Headers are **metadata** for the request — extra information sent alongside the actual data. Examples:
     *
     *   * `Authorization` → credentials/tokens for security.
     *   * `Content-Type` → tells the server how the body is formatted (JSON, XML, etc.).
     *   * `Accept` → tells the server what format you expect back.
     * * In your code:
     *
     *   ```kotlin
     *   @Header("Authorization") authHeader: String
     *   ```
     *
     *   * You’ll pass something like `"Bearer ya29.a0AfH6..."` when calling the function.
     *   * Retrofit puts this in the request:
     *
     *     ```
     *     Authorization: Bearer ya29.a0AfH6...
     *     ```
     *
     * ---
     *
     * ## 3️⃣ `@Body`
     *
     * * **What it means:**
     *   This is the **main content** of the POST request — the data you want to send to the server.
     * * **Why the body exists:**
     *   Unlike GET, where data is sent in the **URL**, POST sends data inside the **HTTP body** (so you can send bigger or more complex data, like JSON objects).
     * * In your code:
     *
     *   ```kotlin
     *   @Body request: FreeBusyRequest
     *   ```
     *
     *   * Retrofit takes your `FreeBusyRequest` object
     *   * Converts it into JSON (using a converter like Gson or Moshi)
     *   * Sends it inside the HTTP body.
     *
     *   Example (simplified):
     *
     *   ```json
     *   {
     *     "timeMin": "2025-08-13T09:00:00Z",
     *     "timeMax": "2025-08-13T17:00:00Z",
     *     "items": [{"id": "someone@example.com"}]
     *   }
     *   ```
     *
     * ---
     *
     * ## 🔄 How They Work Together
     *
     * When you call:
     *
     * ```kotlin
     * val response = api.getFreeBusy(
     *     authHeader = "Bearer ya29.a0AfH6...",
     *     request = myFreeBusyRequest
     * )
     * ```
     *
     * Retrofit builds a request like:
     *
     * ```
     * POST /calendar/v3/freeBusy HTTP/1.1
     * Host: www.googleapis.com
     * Authorization: Bearer ya29.a0AfH6...
     * Content-Type: application/json
     *
     * {
     *   "timeMin": "...",
     *   "timeMax": "...",
     *   "items": [...]
     * }
     * ```
     *
     * ---
     *
     * ✅ **In short:**
     *
     * * `@POST` → Says *what HTTP method* and *where* to send it.
     * * `@Header` → Extra **metadata** for the request (e.g., tokens, content type).
     * * `@Body` → The **main data payload** you’re sending to the server.
     *
     * ---
     *
     */
    @POST("calendar/v3/freeBusy")
    suspend fun getFreeBusy(
        @Header("Authorization") authHeader: String,
        @Body request: FreeBusyRequest
    ): Response<FreeBusyResponse>

        @GET("oauth2/v3/userinfo")
        suspend fun getUserInfo(
        @Header("Authorization") authHeader: String
        ): Response<UserInfoDto>


    /**
     * ---
     *
     * ## 🔹 What is `@Query` in Retrofit?
     *
     * Think of `@Query` as **extra information you write at the end of a website link**.
     *
     * ---
     *
     * ### Example 1 – Simple
     *
     * You have this function:
     *
     * ```kotlin
     * @GET("/friends")
     * fun friends(@Query("page") page: Int): Call<ResponseBody>
     * ```
     *
     * If you call:
     *
     * ```kotlin
     * friends(1)
     * ```
     *
     * Retrofit makes this URL:
     *
     * ```
     * /friends?page=1
     * ```
     *
     * ➡ `"page"` is the **key**
     * ➡ `"1"` is the **value**
     *
     * ---
     *
     * ### Example 2 – Null value
     *
     * If you call:
     *
     * ```kotlin
     * friends(null)
     * ```
     *
     * Retrofit makes:
     *
     * ```
     * /friends
     * ```
     *
     * (no `?page=...` because you gave it `null`)
     *
     * ---
     *
     * ### Example 3 – Multiple values (List or Array)
     *
     * You write:
     *
     * ```kotlin
     * @GET("/friends")
     * fun friends(@Query("id") ids: List<Int>): Call<ResponseBody>
     * ```
     *
     * Call:
     *
     * ```kotlin
     * friends(listOf(1, 2, 3))
     * ```
     *
     * Retrofit makes:
     *
     * ```
     * /friends?id=1&id=2&id=3
     * ```
     *
     * Alright bro, let’s break this **array/varargs + URL encoding** thing in Retrofit into something crystal clear.
     *
     * ---
     *
     * ## 1️⃣ **Array / Varargs in `@Query`**
     *
     * Varargs = “variable arguments” → you can pass multiple values without wrapping them in a list.
     *
     * Example:
     *
     * ```kotlin
     * @GET("/friends")
     * Call<ResponseBody> friends(@Query("group") String... groups);
     * ```
     *
     * If you call:
     *
     * ```java
     * friends("coworker", "bowling");
     * ```
     *
     * Retrofit turns it into:
     *
     * ```
     * /friends?group=coworker&group=bowling
     * ```
     *
     * 💡 Each value becomes **its own `group` parameter** in the URL.
     *
     * ---
     *
     * * **List** →
     *
     *   ```kotlin
     *   friends(listOf("coworker", "bowling"))
     *   ```
     *
     *   → `/friends?group=coworker&group=bowling`
     *
     * * **Vararg** →
     *
     *   ```kotlin
     *   friends("coworker", "bowling")
     *   ```
     *
     *   → `/friends?group=coworker&group=bowling`
     *
     * 💡 In both cases:
     *
     * * The **key** (`group`) is repeated for each value. same key and multiple values .
     * * This is the standard way in URLs to send **multiple values for the same key**.
     *
     * ---
     *
     *
     *
     * ## 🌟 First — what is encoding again?
     *
     * Some characters **can’t go directly into a URL** because they have special meanings.
     * So Retrofit **changes them into safe symbols**.
     *
     * Example:
     *
     * * `"foo bar"` → becomes `foo%20bar`
     *   (space → `%20`)
     * * `"foo+bar"` → becomes `foo%2Bbar`
     *   (`+` → `%2B`)
     *
     * Retrofit **does this automatically** for `@Query` parameters.
     * This is called **URL encoding**.
     *
     * ---
     *
     * ## 🛑 Now — skipping encoding
     *
     * Sometimes you **already** have a value that’s already safe or that you **want to keep exactly the same**, even if it has special characters like `+`.
     *
     * For that, you use:
     *
     * ```kotlin
     * @Query(value = "group", encoded = true)
     * ```
     *
     * `encoded = true` = "Retrofit, don't change my text. Send it as it is."
     *
     * ---
     *
     * ## 📌 Example
     *
     * **Without encoded = true** (default behavior):
     *
     * ```kotlin
     * @GET("/friends")
     * fun friends(@Query("group") group: String)
     * friends("foo+bar")
     * ```
     *
     * Result:
     *
     * ```
     * /friends?group=foo%2Bbar   <-- + was changed to %2B
     * ```
     *
     * ---
     *
     * **With encoded = true**:
     *
     * ```kotlin
     * @GET("/friends")
     * fun friends(@Query(value = "group", encoded = true) group: String)
     * friends("foo+bar")
     * ```
     *
     * Result:
     *
     * ```
     * /friends?group=foo+bar     <-- + stayed as +, not changed
     * ```
     *
     * ---
     *
     * ## 🧠 Why would you skip encoding?
     *
     * * If the server **expects** that `+` means something special and should not be changed.
     * * If your parameter string is **already properly encoded** by you.
     *
     * ---
     *
     */
    @POST("calendar/v3/calendars/primary/events")
    suspend fun createEvent(
        @Header("Authorization") authHeader: String,
        @Query("conferenceDataVersion") conferenceVersion: Int = 1,
        @Body event: MyEventDto
    ): Response<MyEventDto>


    /**
     * Alright bro — let’s break this **`@Path`** thing down step by step so it’s super clear in your head.
     *
     * ---
     *
     * ## 1️⃣ What is `@Path` in Retrofit?
     *
     * `@Path` is for **replacing a placeholder inside the URL path** with a value you pass in.
     *
     * ---
     *
     * ### Example 1 — Simple
     *
     * ```kotlin
     * @GET("/image/{id}")
     * fun example(@Path("id") id: Int): Call<ResponseBody>
     * ```
     *
     * When you call:
     *
     * ```kotlin
     * example(1)
     * ```
     *
     * Retrofit changes `{id}` into `1`:
     *
     * ```
     * /image/1
     * ```
     *
     * 💡 This is different from `@Query` because:
     *
     * * `@Path` → changes **part of the main path** of the URL.
     * * `@Query` → adds data **after the `?`** in the URL.
     *
     * ---
     *
     * ## 2️⃣ URL Encoding (Default)
     *
     * Retrofit **URL encodes** path values automatically so special characters don’t break the URL.
     *
     * Example:
     *
     * ```kotlin
     * @GET("/user/{name}")
     * fun encoded(@Path("name") name: String): Call<ResponseBody>
     *
     * encoded("John%Doe")
     * ```
     *
     * Here `"%"` is a special character in URLs, so Retrofit encodes it:
     *
     * ```
     * /user/John%25Doe
     * ```
     *
     * * `%` → `%25` (safe form)
     *
     * ---
     *
     * ## 3️⃣ Skipping Encoding
     *
     * If you **don’t** want Retrofit to change the value, use:
     *
     * ```kotlin
     * @GET("/user/{name}")
     * fun notEncoded(@Path(value = "name", encoded = true) name: String): Call<ResponseBody>
     *
     * notEncoded("John%Doe")
     * ```
     *
     * Result:
     *
     * ```
     * /user/John%Doe
     * ```
     *
     * 💡 Retrofit sends the value exactly as you wrote it — no changes.
     *
     * ---
     *
     * ## 4️⃣ Path Parameters Cannot Be Null
     *
     * You can’t pass `null` to a `@Path` parameter — because the URL path wouldn’t make sense with a missing piece.
     * Example:
     *
     * ```
     * /image/{id}   ← needs a real value
     * ```
     *
     * ---
     *
     * ✅ **In short:**
     *
     * * `@Path` → Replaces `{placeholder}` in the URL with your value.
     * * Encodes by default → protects special characters.
     * * `encoded = true` → sends your value exactly as you typed it.
     * * Cannot be null → must have a real value.
     *
     * ---
     *
     * Bro, if you want, I can **draw a simple visual** showing the difference between:
     *
     * * `@Path`
     * * `@Query`
     * * `@Body`
     *   so you’ll never mix them up again. Would that help?
     *
     */
    @DELETE("calendar/v3/calendars/primary/events/{eventId}")
    suspend fun deleteEvent(
        @Header("Authorization") authHeader: String,
        @Path("eventId") eventId: String
    ): Response<Unit>


    @GET("calendar/v3/calendars/primary/events/{eventId}")
    suspend fun getAnEvent(
        @Header("Authorization")  authHeader : String ,
        @Path("eventId") eventId : String
    ): Response<MyEventDto>


    /**
     *  What is @PATCH?
     *
     * PATCH is an HTTP method (like GET, POST, PUT, DELETE).
     *
     * It is used to update part of an existing resource on the server (not the whole thing like PUT does).
     *
     * Example:
     *
     * If you have a user profile with 10 fields, but you just want to change the username without touching the rest → PATCH.
     */
    @PATCH("calendar/v3/calendars/primary/events/{eventId}")
    suspend fun updateEvent(
        @Header("Authorization") authHeader: String,
        @Path("eventId") eventId: String,
        @Query("conferenceDataVersion") conferenceVersion: Int = 1,
        @Body updatedEvent: MyEventDto
    ): Response<MyEventDto>
}