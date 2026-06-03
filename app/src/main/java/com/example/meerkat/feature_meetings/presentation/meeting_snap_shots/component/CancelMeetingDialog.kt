package com.example.meerkat.feature_meetings.presentation.meeting_snap_shots.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun CancelMeetingPreview() {

    CancelMeeting({}) { }
}


@Composable
fun CancelMeeting(
    onDismissRequest : () -> Unit ,
    onConfirm : () -> Unit ,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Cancel Meeting")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ){
                Text(text = "Confirm" , color = Color.Red)
            }
        },
        modifier = Modifier,
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            Text(text = "Are you sure you want to cancel this scheduled meeting")
        },
    )
}