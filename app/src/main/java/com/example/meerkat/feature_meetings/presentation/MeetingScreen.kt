package com.example.meerkat.feature_meetings.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meerkat.R
import com.example.meerkat.core.presentation.NoEntries
import com.example.meerkat.core.presentation.SessionExpiredDialog
import com.example.meerkat.core.presentation.ShowUIState
import com.example.meerkat.core.presentation.UIState
import com.example.meerkat.core.presentation.component.LoadingAnimation
import com.example.meerkat.core.presentation.component.showSnackBarUI
import com.example.meerkat.feature_meetings.domain.util.MeetingSnapshot
import com.example.meerkat.feature_meetings.presentation.meeting_snap_shots.MeetingSnapshots
import com.example.meerkat.feature_meetings.presentation.meeting_snap_shots.MeetingSnapshotsEvents
import com.example.meerkat.feature_meetings.presentation.meeting_snap_shots.component.CancelMeeting
import com.example.meerkat.core.presentation.topAppBarColors
import com.example.meerkat.ui.theme.blue400
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun MeetingScreenPreview() {
    val viewModel = hiltViewModel<MeetingScrViewModel>()
    MeetingsScreen(  viewModel  , {} , {})
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingsScreen(
    viewModel: MeetingScrViewModel,
    viewContact: (MeetingSnapshot) -> Unit,
    openFallowUp: (MeetingSnapshot) -> Unit ,
) {
    val sessionDialogState = viewModel.sessionDialogState
    val cancelDialogState = viewModel.cancelMeetingDialogState
    val uiState = viewModel.uiState.collectAsStateWithLifecycle(UIState.Idle)
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        topBar = {
            TopMeetingBar()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        val meetingSnapshots by viewModel.meetingSnapshots.collectAsStateWithLifecycle()
        if(meetingSnapshots.isEmpty()) {
            NoEntries(
                modifier = Modifier.padding(innerPadding),
                image = R.drawable.meetings,
                txt1 = "No meetings have been schedule",
                txt2 = "Sorry,it look no meetings have been schedule on this account"
            )
        }else{
            MeetingSnapshots(
                modifier = Modifier.padding(innerPadding),
                list = meetingSnapshots,
                viewContact = viewContact,
                openFallowUp = openFallowUp,
                onCancelMeeting = { item : MeetingSnapshot ->
                    viewModel.onEvent(MeetingSnapshotsEvents.ShowCancelMeetingDialogState(item))
                }
            )
        }
    }
    if(sessionDialogState.value){
        SessionExpiredDialog(
            onDismiss = { viewModel.onEvent(MeetingSnapshotsEvents.OnDismissSessionDialogState) },
            onLoginAgain = {}
        )
    }
    if(cancelDialogState.value){
        CancelMeeting(
            onDismissRequest = {viewModel.onEvent(MeetingSnapshotsEvents.OnDismissCancelMeetingDialogState)},
            onConfirm = {viewModel.onEvent(MeetingSnapshotsEvents.ConfirmCancelMeeting)}
        )
    }
    ShowUIState(
        uiState = uiState.value,
        onSuccess = {},
        showLoading = {
            LoadingAnimation()
        },
        showMessage = {message ->
            LaunchedEffect(message) {
                showSnackBarUI(
                    snackBarHostState = snackBarHostState,
                    message = message,
                    actionLabel = "",
                    onDismiss = {},
                    onActionPerformed = {}
                )
                delay(100)
                viewModel.clearUiState()
            }
        },
        noAccount = {}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMeetingBar() {
    TopAppBar(
        title = {},
        navigationIcon = {
            Box(
                modifier = Modifier.fillMaxHeight() ,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .size(35.dp)
                )
            }
        } ,
        actions = {
            Box(
                modifier = Modifier.fillMaxHeight() ,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_calendar_view_month_24),
                    contentDescription = "",
                    tint = blue400,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(35.dp)
                )
            }
        } ,
        colors = topAppBarColors(),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
}


@Composable
fun TopBar(
    modifier: Modifier,
    content :     @Composable RowScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(10.dp)
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .height(50.dp)
        ){
            content()
        }
        Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.outlineVariant)
        )
    }
}