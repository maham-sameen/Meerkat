package com.example.meerkat.feature_meetings.presentation.meeting_snap_shots

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.feature_meetings.domain.util.MeetingSnapshot
import com.example.meerkat.feature_meetings.presentation.MeetingActionButton
import com.example.meerkat.feature_meetings.presentation.meeting_snap_shots.component.MeetingDescription


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingSnapshots(modifier: Modifier, list : List<MeetingSnapshot>,
                     viewContact: (MeetingSnapshot) -> Unit,
                     openFallowUp: (MeetingSnapshot) -> Unit ,
                     onCancelMeeting: (MeetingSnapshot) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
        ,
        contentAlignment = Alignment.TopCenter
    ){
        LazyColumn(
            contentPadding = PaddingValues(20.dp) ,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Meeting snapshot",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold ,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
            itemsIndexed(
                items = list,
                key = { index: Int, item: MeetingSnapshot -> index },
            ){ index : Int , item : MeetingSnapshot ->
                MeetingSnapshotSection(
                    meetingSnapshot = item,
                    viewContact = { viewContact(item) },
                    openFallowUp = { openFallowUp(item) },
                    onCancelMeeting = {
                        onCancelMeeting(item)
                    }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingSnapshotSection(meetingSnapshot: MeetingSnapshot,
                           viewContact : () -> Unit,
                           openFallowUp : () -> Unit ,
                           onCancelMeeting : () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
        ,
        shadowElevation = 4.dp ,
        tonalElevation = 4.dp ,
        color = MaterialTheme.colorScheme.surface ,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        MeetingDescription(
            modifier = Modifier.padding(16.dp),
            meetingDescription = meetingSnapshot.meetingDescription,
            timeDetails = meetingSnapshot.timeDetail,
            actionButton = {
                MeetingActionButton(
                    btnTextOne = "View Contact",
                    onBtnTextOne = viewContact,
                    btnTextTwo = "Join meeting",
                    onBtnTextTwo = openFallowUp
                )
            },
            onCancelMeeting = onCancelMeeting
        )
    }
}


