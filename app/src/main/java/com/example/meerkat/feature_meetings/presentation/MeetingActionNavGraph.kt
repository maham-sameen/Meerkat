package com.example.meerkat.feature_meetings.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.meerkat.core.presentation.sharedViewModel
import com.example.meerkat.core.util.Graph
import com.example.meerkat.feature_contacts.presentation.ContactsActionsScreen
import com.example.meerkat.feature_event.presentation.util.BottomBarScreen
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ConfirmMeeting
import com.example.meerkat.feature_meetings.presentation.confirm_meeting.ConfirmMeetingViewModel
import com.example.meerkat.feature_meetings.presentation.meetings_days.MDViewModel
import com.example.meerkat.feature_meetings.presentation.meetings_days.MeetingDays
import com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.MeetingDetailsViewModel
import com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.ScheduleMeetingDetails
import com.example.meerkat.feature_meetings.presentation.schedule_meeting_details.ScheduleMeetingDetailsEvents
import kotlinx.serialization.json.Json


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.meetingActionNavGraph(
    navController : NavHostController ,
    logOut: () -> Unit ,
    onCancelMeeting : () -> Unit ,
    goMeetingScreen : () -> Unit ,
    onEditMeeting : (Long) -> Unit
){
    navigation<Graph.MeetingActions>(
        startDestination = MeetingsActionsScreen.MeetingDays()
    ){
        composable<MeetingsActionsScreen.MeetingDays> {
            val viewModel = it.sharedViewModel<MDViewModel>(navController)
            val eventId = it.toRoute<MeetingsActionsScreen.MeetingDays>().eventId
            MeetingDays(
                eventId = eventId ,
                viewModel = viewModel,
                onLogInAgain = logOut,
                addGuest = { timeSlot : TimeSlot , id : Long? ->
                    val json = Json.encodeToString(timeSlot)
                    navController.navigate(MeetingsActionsScreen.ConfirmMeeting(json , id)){
                        popUpTo(BottomBarScreen.Meetings.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<MeetingsActionsScreen.ConfirmMeeting>{
            val viewModel = it.sharedViewModel<ConfirmMeetingViewModel>(navController)
            val json = it.arguments?.getString("timeSlotJson") ?: return@composable
            val timeSlot = Json.decodeFromString<TimeSlot>(json)
            val eventId = it.toRoute<MeetingsActionsScreen.ConfirmMeeting>().eventId
            ConfirmMeeting(
                eventId = eventId ,
                timeSlot = timeSlot,
                viewModel = viewModel,
                onBackPressed = { navController.popBackStack() },
                onCancelMeeting = onCancelMeeting,
                goMeetingsScreen = goMeetingScreen ,
                onLogInAgain = logOut
            )
        }

        composable<MeetingsActionsScreen.ScheduleMeetingDetails>{
            val viewModel = it.sharedViewModel<MeetingDetailsViewModel>(navController)
            val eventId = it.toRoute<MeetingsActionsScreen.ScheduleMeetingDetails>().eventId
            viewModel.onEvent(ScheduleMeetingDetailsEvents.OnEventId(eventId))
            ScheduleMeetingDetails(
                viewModel, onLoginAgain = logOut,
                onEditMeeting = {
                    onEditMeeting(it)
                }
            )
        }
    }
}

