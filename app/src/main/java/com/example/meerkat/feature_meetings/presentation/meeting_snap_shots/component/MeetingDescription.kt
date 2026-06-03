package com.example.meerkat.feature_meetings.presentation.meeting_snap_shots.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.core.presentation.AddNewEvent
import com.example.meerkat.ui.theme.blue400


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingDescription(modifier: Modifier ,
                       meetingDescription : String,
                       timeDetails : Pair<String , String>,
                       actionButton : @Composable () -> Unit ,
                       onCancelMeeting : () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row (
            verticalAlignment = Alignment.Top
        ){
            Text(
                text = "TOMORROW",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f) ,
                modifier = Modifier.align(Alignment.Top)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Clear ,
                contentDescription = null ,
                tint = blue400 ,
                modifier = Modifier.align(Alignment.Top)
                    .clickable{
                        onCancelMeeting()
                    }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color(0xFF9333EA), CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "30 Minute Meeting",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold ,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight ,
                contentDescription = null ,
                tint = blue400
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        AddNewEvent(
            modifier = Modifier,
            icon = R.drawable.baseline_people_outline_24,
            eventText = meetingDescription,
            iconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
            onClick = {}
        )
        Spacer(modifier = Modifier.height(10.dp))
        AddNewEvent(
            modifier = Modifier,
            icon = R.drawable.baseline_event_24,
            eventText = "${timeDetails.first + "\n" +timeDetails.second } \nGoogle Meet",
            textColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
            iconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
            onClick = {}
        )
        Spacer(modifier = Modifier.height(12.dp))
        actionButton()
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MeetingDescriptionPreview() {
    MeetingDescription(
        modifier = Modifier,
        meetingDescription = "Meeting Description",
        timeDetails = "this is first pair of time details" to "this is the second pair of time details",
        actionButton = {} ,
        onCancelMeeting = {}
    )
}