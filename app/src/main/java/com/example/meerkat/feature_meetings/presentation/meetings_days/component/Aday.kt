package com.example.meerkat.feature_meetings.presentation.meetings_days.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.feature_meetings.domain.util.MeetingDate
import com.example.meerkat.ui.theme.blue400


@Composable
fun Aday(meetingDate: MeetingDate, selectedOption: MeetingDate, onDaySelection: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp) // Add padding for better spacing
            .clickable(onClick = onDaySelection),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = meetingDate.dayName.take(1).uppercase(),
            fontWeight = FontWeight.Bold,
            color = Color.Gray, // Adjust color for better readability
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp)) // Reduce extra spacing

        Box(
            modifier = Modifier
                .size(40.dp) // Adjust circle size
                .clip(CircleShape)
                .background(if (meetingDate == selectedOption) blue400 else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = meetingDate.dayNumber.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (meetingDate == selectedOption) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}