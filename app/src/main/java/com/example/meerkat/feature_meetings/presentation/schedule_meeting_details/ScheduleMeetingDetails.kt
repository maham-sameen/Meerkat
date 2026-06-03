package com.example.meerkat.feature_meetings.presentation.schedule_meeting_details

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meerkat.core.presentation.SessionExpiredDialog
import com.example.meerkat.core.presentation.ShowUIState
import com.example.meerkat.core.presentation.component.LoadingAnimation
import com.example.meerkat.core.presentation.component.showSnackBarUI
import com.example.meerkat.feature_meetings.domain.util.AMeetingDetails
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_meetings.presentation.MeetingActionButton
import com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.component.AttendeeListDetails
import com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.component.HostOverview
import com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.component.MeetingOverview
import com.example.meerkat.ui.theme.blue400
import kotlinx.coroutines.delay
import androidx.core.net.toUri
import com.example.meerkat.core.presentation.UIState


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleMeetingDetails(
    viewModel: MeetingDetailsViewModel ,
    onLoginAgain : () -> Unit ,
    onEditMeeting: (Long) -> Unit
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val UIState = viewModel.uiState.collectAsStateWithLifecycle(UIState.Idle)
    val currentMeetingDetails by viewModel.currentMeetingDetails.collectAsStateWithLifecycle()
    val sessionDialogState =   viewModel.sessionDialogState.collectAsStateWithLifecycle()
    val host by viewModel.host.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity
    Scaffold(
        topBar = {
            MeetingDetailsTopAppBar(
                onEditMeeting = {
                    onEditMeeting(currentMeetingDetails.eventId)
                }
            )
        } ,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            MeetingDetailsScreen(
                currentMeetingDetails, host,
                onJoinMeeting = {
                    activity?.openUrl(currentMeetingDetails.meerkatEventLinks.originalGoogleMeetLink)
                }
            )
            if(sessionDialogState.value){
                SessionExpiredDialog(
                    onDismiss = {  },
                    onLoginAgain = onLoginAgain
                )
            }
            ShowUIState(
                uiState = UIState.value,
                showLoading = {
                    LoadingAnimation()
                },
                showMessage = { message ->
                    LaunchedEffect(message) {
                        showSnackBarUI(
                            snackBarHostState = snackBarHostState,
                            message = message,
                            actionLabel = null,
                            onDismiss = {},
                            onActionPerformed = {}
                        )
                        delay(1000)
                        viewModel.clearUiState()
                    }
                },
                noAccount = {},
                onSuccess = {},
            )
        }
    }
}


@Composable
fun MeetingDetailsScreen(meetingDetails: AMeetingDetails,
                         host: MeetingAttendee ,
                         onJoinMeeting : () -> Unit
) {
    Column (
        modifier = Modifier
    ){
        MeetingOverview(meetingDetails.meerkatEventLinks.meerkatJoinLink)
        HostOverview(host)
        AttendeeListDetails(meetingDetails.attendees)
        Spacer(modifier = Modifier.weight(1f))
        MeetingActionButton(
            modifier = Modifier.padding(start = 14.dp , end = 14.dp , bottom = 30.dp),
            btnTextOne = "Manage",
            onBtnTextOne = {},
            btnTextTwo = "Join meeting",
            onBtnTextTwo = onJoinMeeting
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDetailsTopAppBar(
    onEditMeeting : () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft ,
                contentDescription = null ,
                tint = blue400 ,
                modifier = Modifier.size(35.dp)
            )
        },
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ) ,
        actions = {
            Icon(
                imageVector = Icons.Default.Edit ,
                contentDescription = null ,
                tint = blue400 ,
                modifier = Modifier.size(25.dp)
                    .clickable{
                        onEditMeeting()
                    }
            )
        }
    )
}

fun Activity.openUrl(meetLink : String){
    Intent(Intent.ACTION_VIEW, meetLink.toUri()).also(::startActivity)
}
