package com.example.meerkat.feature_meetings.presentation.confirm_meeting.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ContactAttendee
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.MeetingAttendeeItem

@Composable
fun AddGuestSuggestion(
    contacts : List<ContactAttendee>,
    onSelectGuest : (item : ContactAttendee) -> Unit
) {
    LazyColumn( // This can stay as a nested scroll
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
        , // <- limit height if needed
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(contacts) { index, item ->
            MeetingAttendeeItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelectGuest(item)
                    },
                meetingAttendee = item.attendee
            )
        }
    }
}