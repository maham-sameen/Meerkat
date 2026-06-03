package com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.MeetingAttendeeItem


@Composable
fun HostOverview(host : MeetingAttendee) {
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Host",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Surface(
            color = MaterialTheme.colorScheme.surface ,
            shape = RoundedCornerShape(5.dp ) ,
            border = BorderStroke(1.dp , MaterialTheme.colorScheme.onSurface.copy(0.3f))
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                MeetingAttendeeItem(modifier = Modifier.padding(6.dp) , host)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight ,
                    contentDescription = null ,
                    tint = MaterialTheme.colorScheme.onSurface.copy(0.3f) ,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Invitee details",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
