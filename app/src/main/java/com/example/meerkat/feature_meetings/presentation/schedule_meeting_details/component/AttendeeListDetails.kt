package com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meerkat.R
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.core.presentation.AddNewEvent
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ContactAttendee
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.MeetingAttendeeItem
import com.example.meerkat.ui.theme.blue400


@Composable
fun AttendeeListDetails(list: SnapshotStateList<ContactAttendee>) {

    // why we use the BoxWithConstraints
    //LazyRow measures each item based on its content, not the full screen
    //LazyRow lays out items horizontally, so it gives each item unconstrained width.

    BoxWithConstraints {
        val screenWidth = this.maxWidth - 20.dp
        LazyRow(
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = list,
                key = { index: Int, _: ContactAttendee -> index }
            ) { index: Int, item: ContactAttendee ->
                Box(modifier = Modifier.width(screenWidth)) {
                    AttendeeItemDetails(
                        meetingAttendeeItem = item.attendee,
                        contactEntityItem = item.contact
                    )
                }
            }
        }
    }
}


@Composable
fun AttendeeItemDetails(meetingAttendeeItem : MeetingAttendee , contactEntityItem : ContactEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(5.dp) ,
        color = MaterialTheme.colorScheme.surface ,
        border = BorderStroke(2.dp , MaterialTheme.colorScheme.onSurface.copy(0.3f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            MeetingAttendeeItem(modifier = Modifier.fillMaxWidth() , meetingAttendeeItem)
            Spacer(modifier = Modifier.height(7.dp))
            Column(
                modifier = Modifier.padding(start = 5.dp)
            ) {
                AddNewEvent(
                    icon = R.drawable.baseline_event_24,
                    eventText = "Scheduled",
                    iconColor = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(10.dp))
                AddNewEvent(
                    icon = R.drawable.baseline_email_24,
                    eventText = contactEntityItem.email,
                    textColor = blue400,
                    iconColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(25.dp))
                AddNewEvent(
                    icon = R.drawable.baseline_phone_android_24,
                    eventText = contactEntityItem.phoneNumber.countryCode + contactEntityItem.phoneNumber.phoneNumber,
                    textColor = blue400,
                    iconColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier,
                    onClick = {}
                )
            }
        }
    }
}

