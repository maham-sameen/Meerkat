package com.example.meerkat.feature_meetings.presentation.confirm_meeting.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ContactAttendee
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.MeetingAttendeeItem

@Composable
fun SelectedGuest(
    selectGuest : List<ContactAttendee> ,
    onCancel : (item : ContactAttendee) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(selectGuest) { index, item ->
            SelectedGuestItem(
                meetingAttendee = item.attendee,
                onCancel = {
                    onCancel(item)
                }
            )
        }
    }
}



@Composable
fun SelectedGuestItem(meetingAttendee: MeetingAttendee, onCancel : () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth() ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MeetingAttendeeItem(
            modifier = Modifier ,
            meetingAttendee  = meetingAttendee
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Clear ,
            contentDescription = null ,
            tint = MaterialTheme.colorScheme.onSurface ,
            modifier = Modifier
                .clickable{
                    onCancel()
                }
        )
    }
}