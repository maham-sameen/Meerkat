package com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.core.presentation.AddNewEvent
import com.example.meerkat.ui.theme.blue400


@Composable
fun MeetingOverview(jointLink : String) {
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = "Meeting details",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        AddNewEvent(
            icon = R.drawable.outline_videocam_24,
            eventText = jointLink,
            textColor = blue400,
            iconColor = MaterialTheme.colorScheme.onSurface.copy(0.6f),
            onClick = {}
        )
        Spacer(modifier = Modifier.height(10.dp))
        AddNewEvent(
            icon = R.drawable.baseline_event_24,
            eventText = "30 Minutes Meetings",
            modifier = Modifier,
            iconColor = MaterialTheme.colorScheme.onSurface.copy(0.6f),
            onClick = {}
        )
    }
}