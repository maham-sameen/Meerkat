package com.example.meerkat.feature_meetings.presentation.confirm_meeting.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.core.presentation.AddNewEvent


@Composable
fun MeetingExtras(
    eventDescription : String ,
    timeDetails : String
) {
    Column {
        Text(
            text = "Meeting details" ,
            fontSize = 20.sp ,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        AddNewEvent(
            icon = R.drawable.baseline_calendar_today_24,
            modifier = Modifier,
            eventText = eventDescription,
            textColor = MaterialTheme.colorScheme.onSurface,
            iconColor = MaterialTheme.colorScheme.onSurface,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(20.dp))
        AddNewEvent(
            icon = R.drawable.baseline_event_24,
            modifier = Modifier,
            eventText = timeDetails,
            textColor = MaterialTheme.colorScheme.onSurface,
            iconColor = MaterialTheme.colorScheme.onSurface,
            onClick = {}
        )
    }
}