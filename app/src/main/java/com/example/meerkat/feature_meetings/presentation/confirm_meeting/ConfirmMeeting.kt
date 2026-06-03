package com.example.meerkat.feature_meetings.presentation.confirm_meeting


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meerkat.core.presentation.SessionExpiredDialog
import com.example.meerkat.core.presentation.ShowUIState
import com.example.meerkat.core.presentation.component.FilledButtonWithBlueBack
import com.example.meerkat.core.presentation.component.LoadingAnimation
import com.example.meerkat.core.presentation.component.showSnackBarUI
import com.example.meerkat.feature_meetings.domain.util.MeetingAttendee
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.component.AddGuestSuggestion
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.component.AddGuestTextField
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.component.AttendeesExtras
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.component.InviteeQuestions
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.component.MeetingExtras
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.component.SelectedGuest
import com.example.meerkat.ui.theme.blue400
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmMeeting(
    eventId : Long? = null ,
    onBackPressed: () -> Unit ,
    onCancelMeeting: () -> Unit ,
    timeSlot: TimeSlot ,
    viewModel : ConfirmMeetingViewModel ,
    goMeetingsScreen : () -> Unit ,
    onLogInAgain : () -> Unit
) {
    val screenScrollState = rememberLazyListState()
    val timeDetails by  viewModel.timeDetail
    val host  by viewModel.host.collectAsStateWithLifecycle()

    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val contacts by viewModel.filteredContacts.collectAsStateWithLifecycle()
    val selectGuest by viewModel.selectedGuest.collectAsStateWithLifecycle()
    val UIState  =  viewModel.uiState.collectAsStateWithLifecycle()
    val sessionDialogState  by viewModel.sessionDialogState

    if(sessionDialogState) {
        SessionExpiredDialog(
            onDismiss = { viewModel.onEvent(ConfirmMeetingEvents.OnDismiss) },
            onLoginAgain = onLogInAgain
        )
    }
    Box{
        LaunchedEffect(Unit) {
            viewModel.onEvent(ConfirmMeetingEvents.SetTimeDetail(timeSlot))
            if(eventId!= null){
                viewModel.onEvent(ConfirmMeetingEvents.ToUpdateConfirmMeeting(eventId))
            }
        }
    }
    LaunchedEffect(selectGuest.size) {
        // Only run when a guest is added
        println("Inside this launched effect see what happens selectedGuestSize : ${selectGuest.size} ")
        if (selectGuest.isNotEmpty()) {
            snapshotFlow { screenScrollState.layoutInfo.totalItemsCount }
                .collect { totalItems ->
                    println("total Item : $totalItems")
                    if (totalItems > 0) {
                        // Give LazyColumn time to lay out
                        awaitFrame()
                        screenScrollState.animateScrollToItem(totalItems - 1)
                    }
                }
        }
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            ConfirmMeetingTopAppBar(
                onBackPressed = onBackPressed,
                onCancelMeeting = onCancelMeeting
            )
        }
    ) {innerPadding->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            Column {
                HorizontalDivider()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    state = screenScrollState
                ) {
                    item {
                        MeetingExtras(
                            eventDescription = "30 Minutes Meeting,30 mins,Google Meet",
                            timeDetails = "${timeDetails.first} \n${timeDetails.second} \n,Google Meet"
                        )
                    }

                    item {
                        AttendeesExtras(
                            host = host?.attendee ?: MeetingAttendee(
                                profileText = "Y",
                                name = "You",
                                isHost = true
                            ),
                            addGuestSuggestions = {
                                Column (
                                    modifier = Modifier.background(
                                        MaterialTheme.colorScheme.surfaceContainer,
                                RoundedCornerShape(20.dp)
                                )
                                .padding(10.dp)
                                ){
                                    AddGuestTextField(
                                        text = searchText,
                                        onTextChanged = {
                                            viewModel.onEvent(
                                                ConfirmMeetingEvents.OnSearchText(
                                                    it
                                                )
                                            )
                                        },
                                        onDone = {
                                            viewModel.onEvent(ConfirmMeetingEvents.OnSearchTextDone)
                                        },
                                        modifier = Modifier
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    AddGuestSuggestion(
                                        contacts = contacts,
                                        onSelectGuest = {
                                            viewModel.onEvent(
                                                ConfirmMeetingEvents.OnSelectGuest(
                                                    it
                                                )
                                            )
                                        }
                                    )
                                }
                            },
                            selectedGuests = {
                                SelectedGuest(
                                    selectGuest = selectGuest,
                                    onCancel = {
                                        viewModel.onEvent(ConfirmMeetingEvents.OnUnSelectGuest(it))
                                    }
                                )
                            },
                        )
                    }

                    item {
                        InviteeQuestions(
                            addNote = {

                            }
                        )
                    }

                    item {
                        FilledButtonWithBlueBack(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            text = "Schedule meeting",
                            elevation = 5.dp,
                            onClick = {
                                viewModel.onEvent(ConfirmMeetingEvents.ScheduleMeeting)
                            }
                        )
                    }
                }
            }
            val context = LocalContext.current
            ShowUIState(
                uiState = UIState.value,
                showLoading = {
                    LoadingAnimation()
                },
                showMessage = { message ->
                    println("we are showing the message")
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
                        goMeetingsScreen()
                    }
                },
                noAccount = {},
                onSuccess = {
                    LaunchedEffect(Unit) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                            if(ContextCompat.checkSelfPermission( context, Manifest.permission.POST_NOTIFICATIONS)
                                == PackageManager.PERMISSION_GRANTED){
                                println("we are going to show  notifications")
                                viewModel.showNotification()

                            }
                        }
                        delay(100)
                        viewModel.clearUiState()
                        goMeetingsScreen()
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmMeetingTopAppBar(
    onBackPressed : () -> Unit ,
    onCancelMeeting : () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Confirm meeting" ,
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold ,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier,
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft ,
                contentDescription = "back" ,
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        onBackPressed()
                    }
                ,
                tint = blue400
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Clear ,
                contentDescription = "cross" ,
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        onCancelMeeting()
                    }
                ,
                tint = blue400
            )
        },
    )
}

/**
 *
 *  Snapshot :
 *  A snapshot is like taking the photo of  states at that exact moment so it capture the current version or value fo all states at that moment .
 *  while you are inside that snapshot , reading any states will give you the value from the moment you take snapshots .
 *
 *  KeyPoints  :
 *  1. All state object will have the same value in the snapshot
 *  if you take the snapshot at time T then counter was 5 at that time and after some time you read that value then it will be 5 even if in real world counter
 *  may change to the 6 .
 *
 *  2.To enter snapshot call enter
 *  Inside enter block , all state current version at that moment  can be read when you leave block , you leave the snapshots .
 *
 *
 *
 *  SnapshotFlow :
 *  The block may run multiple times even if your input are sames .
 *  If your block changes multiple times , your block will be delayed and run only once after those changes settle .
 *  For example if state changes from A to B to C To D in milliseconds instead running of block 4 times then block might just run only once to D .
 *
 *  It should be idempotent : means running it multiple time with same input will result same output .
 *  Example: If the block adds 5 to a number, it’s not idempotent (because running it twice changes the result again). But if the block sets the number
 *  to 5, it is idempotent.
 *
 *  and free of side effect.
 *
 *  state = current value(latest data )
 *  event = a change that happened at specific moment .
 *
 *  you can not model or store all events into the state object because
 *  state only remember the latest value or current value but event may trigger multiple time with same value .
 *
 *  observable state is lossy compression of the event that produce it . here lossy compression means when you covert the events into state
 *  you lose some details how you got there .
 *  Example:
 * If your event stream was 1 → 2 → 1 → 2,
 * the state might just show 2 at the end — you lose the fact that it went back and forth.
 *
 * 5. “An observable event happens at a point in time and is discarded.”
 * Events are one-time.
 *
 * Once they happen and observers are notified, they’re gone.
 *
 * 6. “All registered observers at the time the event occurred are notified.”
 * Only the listeners that are watching at that moment will get the event.
 *
 * every events matters means if the same event occurs twice then it is important even if it send the same value ,
 * event can depend on top of each other , later event may depend on the earlier one .
 *
 * states only tells you the latest value and only notifies you when when value changes to something different .
 * If state changes a bunch in a short time, you might only get the final change — earlier ones are skipped.
 *
 * Example: If value changes A → B → C very fast, your observer might just see C.
 *
 *
 */
