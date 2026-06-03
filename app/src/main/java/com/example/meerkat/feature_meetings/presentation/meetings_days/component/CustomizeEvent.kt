package com.example.meerkat.feature_meetings.presentation.meetings_days.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.core.presentation.AddNewEvent
import com.example.meerkat.R
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.ui.theme.blue400


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CustomizeEventBottomSheet() {
    val customizeEventState = rememberModalBottomSheetState()
    val showCustomizeEvent = remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding) ,
            contentAlignment = Alignment.Center
        ){
            Text(text = "open the bottom sheet" , modifier = Modifier
                .clickable{showCustomizeEvent.value = true})
            if(showCustomizeEvent.value){
                ModalBottomSheet(
                    onDismissRequest = {showCustomizeEvent.value = false},
                    sheetState = customizeEventState
                ) {
                    CustomizeOptions(
                        modifier = Modifier, null,
                        addGuest = {}
                    )
                }
            }
        }
    }
}


@Composable
fun CustomizeOptions(
    modifier: Modifier ,
    host : MeetingAttendee? ,
    addGuest : () -> Unit
) {
        Column(
            modifier = modifier.padding(20.dp)
        ) {
            Text(
                text = "The event",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            AddNewEvent(
                icon = R.drawable.baseline_event_24,
                eventText = "Event name: 30 Minute Meetings",
                onClick = {}
            )
            AddNewEvent(
                icon = R.drawable.outline_nest_clock_farsight_analog_24,
                eventText = "Duration: 30 minutes",
                onClick = {}
            )
            AddNewEvent(
                icon = R.drawable.outline_edit_location_24,
                eventText = "Location: Google Meet",
                onClick = {}
            )
            AddNewEvent(
                icon = R.drawable.outline_kid_star_24,
                eventText = "Hosts",
                onClick = {}
            )
            Row(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                MeetingAttendeeItem(modifier = Modifier , meetingAttendee = host ?: MeetingAttendee(
                    profileText = "Y",
                    name = "You",
                    isHost = true
                ))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick =addGuest ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = blue400 ,
                    contentColor = Color.White
                ) ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = "Add Guest"
                )
            }
            Spacer(modifier = Modifier.height(60.dp))
       }
}