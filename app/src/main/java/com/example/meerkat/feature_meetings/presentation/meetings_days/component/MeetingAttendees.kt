package com.example.meerkat.feature_meetings.presentation.meetings_days.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_contacts.presentation.component.ContactLogo



@Composable
fun MeetingAttendeeItem(
    modifier: Modifier  = Modifier,
    meetingAttendee: MeetingAttendee
) {
    Row(
        modifier = modifier
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactLogo(
            modifier = Modifier.size(35.dp)
        ) {
            Text(text = meetingAttendee.profileText, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = meetingAttendee.name, style = MaterialTheme.typography.bodyMedium)
    }
}
