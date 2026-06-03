package com.example.meerkat.feature_event.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.core.presentation.AddNewEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingAvailableOptions() {

    val meetingOptionBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Ensures it either fully shows or hides
    )
    var meetingOptionsSheet by remember { mutableStateOf(false) }
    val shareOptionsBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Ensures it either fully shows or hides
    )
    var shareOptionsSheet by remember { mutableStateOf(false) }
    

    Scaffold {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize() , contentAlignment = Alignment.Center){
            Text("open the bottom sheet " , modifier = Modifier.clickable{meetingOptionsSheet = true})
        }
        if (meetingOptionsSheet) {
            ModalBottomSheet(
                onDismissRequest = { meetingOptionsSheet = false }, // Closes when clicking outside
                sheetState = meetingOptionBottomSheetState,
                scrimColor = Color.Transparent
            ) {
                MeetingOptions(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
                    meetingTitle = "30 Minutes Meeting",
                    meetingType = "One-on-one,30 mins",
                    onCopyLink = {},
                    onCopySingleUseLink = {},
                    onMoreShareOptions = {
                        shareOptionsSheet = true
                    },
                )
            }
        }
        if(shareOptionsSheet){
            ModalBottomSheet(
                onDismissRequest = { shareOptionsSheet = false }, // Closes when clicking outside
                sheetState = shareOptionsBottomSheetState,
                scrimColor = Color.Transparent
            ){
                MoreShareOptions(
                    modifier = Modifier.fillMaxHeight(0.3f).fillMaxWidth().padding(20.dp),
                    onSendEmail = {},
                    onSendText = {},
                    onShareVia = {}
                )
            }
        }
    }
}


@Composable
fun MeetingOptions(
    modifier  : Modifier ,
    meetingTitle : String ,
    meetingType : String,
    onCopyLink : () -> Unit ,
    onCopySingleUseLink : () -> Unit ,
    onMoreShareOptions : () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier  = Modifier.height(20.dp))
            Text(
                text =  meetingTitle ,
                fontSize = 25.sp ,
                fontWeight = FontWeight.Bold ,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = meetingType ,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Spacer(modifier  = Modifier.height(20.dp))
            MeetingShareOptions(
                onCopyLink = onCopyLink,
                onCopySingleUseLink = onCopySingleUseLink,
                onMoreShareOptions = onMoreShareOptions
            )
        }
    }
}


@Composable
fun MeetingShareOptions(
    onCopyLink : () -> Unit ,
    onCopySingleUseLink : () -> Unit ,
    onMoreShareOptions : () -> Unit
) {
    Column {
        AddNewEvent(
            icon = R.drawable.outline_content_copy_24,
            eventText = "Copy link",
            onClick = onCopyLink
        )
//        AddNewEvent(
//            icon = R.drawable,
//            eventText = "Copy single-use link",
//            onClick = onCopySingleUseLink
//        )
        AddNewEvent(
            icon = R.drawable.outline_share_24,
            eventText = "More share options",
            onClick = onMoreShareOptions
        )
    }
}


@Composable
fun MoreShareOptions(
    modifier: Modifier ,
    onSendEmail : ()  -> Unit ,
    onSendText : () -> Unit ,
    onShareVia : () -> Unit
) {
    Column(
        modifier  = modifier
    ) {
        AddNewEvent(
            icon = R.drawable.baseline_email_24,
            eventText = "Send email",
            onClick = {}
        )
        AddNewEvent(
            icon = R.drawable.outline_send_24,
            eventText = "Send text",
            onClick = {}
        )
        AddNewEvent(
            icon = R.drawable.outline_qr_code_24,
            eventText = "Create QR code",
            onClick = {}
        )
        AddNewEvent(
            icon = R.drawable.outline_more_horiz_24,
            eventText = "Share via",
            onClick = {}
        )
    }
}




@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun MeetingAvailableOptionsPreview() {
    MeetingAvailableOptions()
}