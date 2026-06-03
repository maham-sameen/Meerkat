package com.example.meerkat.feature_meetings.data.data_source.model.remote

import com.example.meerkat.feature_meetings.domain.model.BusyTime


/**
 *
 * #### `kind: string`
 *
 * * It just tells the type of response.
 * * Here, it will always be `"calendar#freeBusy"` → means this response is about Free/Busy info from Google Calendar.
 *
 * #### `timeMin: datetime`
 *
 * * This is the **start time** of the range you asked for.
 * * Example: `"2024-09-01T09:00:00Z"` → check if someone is free or busy **from this time**.
 *
 * #### `timeMax: datetime`
 *
 * * This is the **end time** of the range.
 * * Example: `"2024-09-01T18:00:00Z"` → check up to this time.
 *
 * ---
 *
 * ### 🔷 `groups: object`
 *
 * This part is only used **if you are checking the availability of a group**, like a Google Group (e.g., [team@company.com](mailto:team@company.com)).
 *
 * #### `groups.(key): nested object`
 *
 * * `key` is the group’s email (like `"dev-team@yourcompany.com"`).
 * * The value is a list of calendars that belong to that group.
 *
 * #### `groups.(key).calendars`
 *
 * * This is a **list of calendar IDs** (emails) that belong to the group.
 *
 * ---
 *
 * ### 🔷 Handling Errors in Group Expansion
 *
 * Sometimes, Google can't get the group info properly. In such cases, it adds **errors**.
 *
 * #### `groups.(key).errors[]`
 *
 * * A list of errors that occurred while trying to expand that group into individual calendars.
 *
 * ##### `domain: string`
 *
 * * Describes **where the error happened**.
 * * For example: `"calendar"` means it’s related to calendar service.
 *
 * ##### `reason: string`
 *
 * * Tells the **specific problem**.
 * * Some possible values:
 *
 *   * `"groupTooBig"`: Too many members in the group to process.
 *   * `"tooManyCalendarsRequested"`: You asked for info from too many calendars in one request.
 *   * `"notFound"`: The group or user was not found.
 *   * `"internalError"`: Something went wrong on Google’s side (server crash, bug, etc).
 *
 * ⚠️ **Note:** There might be more error types in future. So your app should be ready to handle **unknown errors** too.
 *
 * ---
 *
 * ### ✅ Example
 *
 * Suppose you're asking Google:
 *
 * > "Tell me who's busy in the [dev-team@mycompany.com](mailto:dev-team@mycompany.com) group between 9 AM and 5 PM on Sept 1st."
 *
 * * If everything works: you get a list of calendars inside that group, and who is busy when.
 * * If something goes wrong (like too many members), you’ll get an `errors[]` with the reason like `"groupTooBig"`.
 *
 * ---
 * ### 🔹 `groups.(key).calendars[]`
 *
 * ➡️ This gives you a list of **calendar IDs inside a group**.
 * For example, if you check `group@company.com`, this might return:
 *
 * ```json
 * ["user1@company.com", "user2@company.com"]
 * ```
 *
 * ---
 *
 * ### 🔹 `calendars`
 *
 * ➡️ This section gives the **free/busy data** for each calendar ID you requested (or got from a group).
 *
 * ---
 *
 * ### 🔹 `calendars.(key)`
 *
 * ➡️ This gives free/busy info **for one specific calendar**, like `user1@company.com`.
 *
 * ---
 *
 * ### 🔹 `calendars.(key).errors[]`
 *
 * ➡️ If there was a problem while checking that person’s calendar, the error will be listed here.
 *
 * Example:
 *
 * ```json
 * {
 *   "domain": "global",
 *   "reason": "notFound"
 * }
 * ```
 *
 * ---
 *
 * ### 🔹 Error `reasons` meanings:
 *
 * * `"groupTooBig"` → You asked for a group that has **too many members** (limit exceeded).
 * * `"tooManyCalendarsRequested"` → You asked for **too many calendars** in one query (max is 50).
 * * `"notFound"` → The calendar **doesn’t exist**.
 * * `"internalError"` → Some **internal issue** on Google's side.
 *
 * ---
 *
 * ### 🔹 `calendars.(key).busy[]`
 *
 * ➡️ List of **time ranges** when that calendar is **busy**.
 *
 * Example:
 *
 * ```json
 * [
 *   {
 *     "start": "2025-07-25T10:00:00Z",
 *     "end": "2025-07-25T11:00:00Z"
 *   }
 * ]
 * ```
 *
 * ---
 *
 * ### 🔹 `start` and `end`
 *
 * * `start` = When the busy time **starts** (inclusive).
 * * `end` = When the busy time **ends** (exclusive — not included in busy time).
 *
 * ---
 *
 * ✅ So, this section tells you:
 * **Who is busy, when, and if any errors happened while checking their calendars.**
 *
 * Let me know if you want a real-world example too, bro.
 *
 *
 *
 */

data class FreeBusyResponse(
    val kind: String,
    val timeMin: String,
    val timeMax: String,
    val groups: Map<String, GroupInfo> = emptyMap(),
    val calendars: Map<String, CalendarInfo> = emptyMap()
)

data class GroupInfo(
    val errors: List<ErrorInfo>? = null,
    val calendars: List<String>? = null
)

data class CalendarInfo(
    val errors: List<ErrorInfo>? = null,
    val busy: List<BusyTime>? = null
)

data class ErrorInfo(
    val domain: String,
    val reason: String
)
