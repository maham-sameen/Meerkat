package com.example.meerkat.feature_meetings.domain.use_case.meeting_snap_shots

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.Constants
import com.example.meerkat.core.calendar.data.data_source.model.local.toTimeSlot
import com.example.meerkat.core.calendar.domain.use_case.GetMeerkatEventLinks
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.use_case.GetMeetingDescription
import com.example.meerkat.feature_meetings.domain.use_case.FormateTimeSlotDetailed
import com.example.meerkat.feature_meetings.domain.use_case.MapContactToAttendee
import com.example.meerkat.feature_meetings.domain.util.MeetingSnapshot
import com.example.meerkat.feature_meetings.domain.util.distinctUntilListChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

class GetMeetingSnapShots(
    private val meetingRepo: MeetingRepo,
    private val formateTimeSlotDetailed: FormateTimeSlotDetailed,
    private val mapContactToAttendee: MapContactToAttendee,
    private val getMeerkatEventLinks: GetMeerkatEventLinks,
    private val getMeetingDescription: GetMeetingDescription
) {

    /**
     * Yes bro, you're **100% right** — let's break it down clearly and confirm everything you're thinking. You're spot on, but here's the deep explanation:
     *
     * ---
     *
     * ### ✅ 1. **What's Happening in `flatMapLatest`**
     *
     * ```kotlin
     * meetingConfirmationRepo.getScheduledEventsIds() // Flow<List<ScheduledEventId>>
     *     .mapLatest { scheduledEventIds ->
     *         val eventIds = scheduledEventIds.map { it.scheduledEventId }
     *         eventIds
     *     }
     *     .flatMapLatest { eventIds ->
     *         meetingConfirmationRepo.getEventWithContacts(eventIds) // Flow<List<EventWithContacts>>
     *             .mapLatest { ... } // Produces Flow<List<MeetingSnapshot>>
     *     }
     * ```
     *
     * ### ✅ So yes, bro — you're absolutely right:
     *
     * ### ✅ 2. **`flatMapLatest` works like this:**
     *
     * > Every time the **outer flow emits a new value**, it:
     *
     * * **Cancels** the collection of the inner flow (which was made using `getEventWithContacts(eventIds)`)
     * * **Launches a new coroutine** to collect from the new inner flow
     *
     * ✅ This ensures that **only the latest `eventIds` are used**, and any work for the previous IDs is **cancelled immediately**.
     *
     * ---
     *
     * ### 💡 Summary of Each Flow Function:
     *
     * | Operator          | Role                                                                 |
     * | ----------------- | -------------------------------------------------------------------- |
     * | `mapLatest`       | Transforms the items (e.g., from `ScheduledEventId` to `Long`)       |
     * | `flatMapLatest`   | **Cancels previous inner flow** and switches to collecting a new one |
     * | Inner `mapLatest` | Maps each `EventWithContacts` to a `MeetingSnapshot` list            |
     *
     * ---
     *
     * ### ✅ 3. **Do they run on the same coroutine?**
     *
     * Yes — they **all run within the same coroutine scope**, unless you explicitly switch using `flowOn()` or use another `CoroutineScope`. But here's what happens:
     *
     * * `flatMapLatest` launches a **new coroutine for the inner flow**, but inside the **same coroutine context**
     * * When a new outer value comes in, the coroutine running the previous inner flow is **automatically cancelled**
     * * This gives you **latest-only behavior** without manual job tracking
     *
     * ---
     *
     * ### ✅ Visualization
     *
     * ```
     * Outer Flow (IDs):  ---A-------------B------------
     *                          |
     *                          | cancel A
     *                          ↓
     * Inner Flow:        --------> snapshots from B ----
     * ```
     *
     * ---
     *
     * ### ✅ Final Verdict
     *
     * > 🔥 Yes bro, your understanding is correct:
     *
     * * You collect from the outer flow (scheduled event IDs)
     * * You use those IDs to create a new inner flow (`getEventWithContacts`)
     * * `flatMapLatest` **cancels** the previous inner flow if a new one comes
     * * All of this happens in a coroutine-friendly, cancellable way
     *
     * ---
     *
     * Let me know if you want a diagram or a mini experiment to prove this with logs!
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(timeZone : String): Flow<List<MeetingSnapshot>> {
        Timber.tag(Constants.appName).i("\uD83E\uDEDB [GetMeetingSnapShots] , timeZone : $timeZone")
        return meetingRepo.getEventWithContacts(timeZone)
            .distinctUntilListChanged()
            .mapLatest { eventWithContactsList ->
                Timber.tag(Constants.appName).i("\uD83E\uDEDB [GetMeetingSnapShots] List of event with contacts ${eventWithContactsList.size}")
                eventWithContactsList.mapNotNull { eventWithContacts ->
                    Timber.tag(Constants.appName).i("\uD83E\uDEDB [GetMeetingSnapShots]  eventWithContacts :  contact ${eventWithContacts.contacts} \n event ${eventWithContacts.event}")
                    val event = eventWithContacts.event
                    val hangoutLink = event.hangoutLink ?: return@mapNotNull null
                    Timber.tag(Constants.appName).i("\uD83E\uDEDB [GetMeetingSnapShots]  hangoutLink is not null")
                    MeetingSnapshot(
                        eventId = event.eventPrimaryId,
                        meerkatEventLinks = getMeerkatEventLinks.generateJoinLinkPair(
                            event.eventId,
                            hangoutLink
                        ),
                        timeDetail = formateTimeSlotDetailed(event.toTimeSlot()),
                        meetingDescription = getMeetingDescription(
                            eventWithContacts.contacts.map(mapContactToAttendee::invoke)
                        )
                    )
                }
            }
    }
}