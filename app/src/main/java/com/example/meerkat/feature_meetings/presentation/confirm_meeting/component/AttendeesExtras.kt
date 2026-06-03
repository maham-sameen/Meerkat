package com.example.meerkat.feature_meetings.presentation.confirm_meeting.component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.MeetingAttendeeItem
import com.example.meerkat.ui.theme.blue400


@Composable
fun AttendeesExtras(
    host : MeetingAttendee,
    addGuestSuggestions :  @Composable  () -> Unit,
    selectedGuests : @Composable () -> Unit
) {
    Column {
        Text(
            text = "Attendees" ,
            fontSize = 20.sp ,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            MeetingAttendeeItem(modifier = Modifier.padding(6.dp), host)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Host" ,
                color = blue400 ,
                fontWeight = FontWeight.Bold ,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Selected Guest" ,
            fontWeight = FontWeight.Bold ,
            color = MaterialTheme.colorScheme.onSurface ,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        selectedGuests()
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Add guest by entering either their name or email" ,
            fontWeight = FontWeight.Bold ,
            color = blue400
        )
        Spacer(modifier = Modifier.height(10.dp))
        addGuestSuggestions()
    }
}