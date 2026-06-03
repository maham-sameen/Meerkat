package com.example.meerkat.feature_event.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.meerkat.core.presentation.sharedViewModel
import com.example.meerkat.core.util.Graph
import com.example.meerkat.feature_contacts.ContactsScreen
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.presentation.ContactsActionsScreen
import com.example.meerkat.feature_contacts.presentation.NewContViewModel
import com.example.meerkat.feature_contacts.presentation.contactsActionNavGraph
import com.example.meerkat.feature_event.presentation.util.BottomBarScreen
import com.example.meerkat.feature_meetings.presentation.MeetingScrViewModel
import com.example.meerkat.feature_meetings.presentation.MeetingsActionsScreen
import com.example.meerkat.feature_meetings.presentation.MeetingsScreen
import com.example.meerkat.feature_meetings.presentation.meetingActionNavGraph
import com.example.meerkat.feature_notification.presentation.NotifViewModel
import com.example.meerkat.feature_notification.presentation.NotificationsScreen
import com.example.meerkat.feature_setting.presentation.settingNavGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavGraph(
    navController: NavHostController ,
    logOut : () -> Unit
) {
    NavHost(
       startDestination = BottomBarScreen.Home.route,
        navController = navController,
    ) {
        composable(route = BottomBarScreen.Home.route){
            val parentEntry = remember(it) { navController.getBackStackEntry(BottomBarScreen.Home.route) } // your nav graph route
            val viewModel: HomeViewModel = hiltViewModel(parentEntry)
            HomeScreen(
                onPickImage = {
                    navController.navigate(Graph.Settings) {
                        popUpTo(Graph.Settings) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onBookMeeting = {
                    navController.navigate(MeetingsActionsScreen.MeetingDays())
                },
                onCreateNewContact = {
                    navController.navigate(ContactsActionsScreen.CreateContact()) {
                        popUpTo(BottomBarScreen.Contacts.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                homeViewModel = viewModel,
            )
        }
        composable(
            route = BottomBarScreen.Meetings.route
        ){ it ->
            val parentEntry = remember(it) { navController.getBackStackEntry(BottomBarScreen.Meetings.route) } // your nav graph route
            val viewModel: MeetingScrViewModel = hiltViewModel(parentEntry)
            MeetingsScreen(
                viewModel = viewModel,
                viewContact = {},
                openFallowUp = { meetingSnapshot ->
                    navController.navigate(MeetingsActionsScreen.ScheduleMeetingDetails(meetingSnapshot.eventId)) {
                        popUpTo(BottomBarScreen.Meetings.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(route = BottomBarScreen.Contacts.route){
            val parentEntry = remember(it) { navController.getBackStackEntry(BottomBarScreen.Contacts.route) } // your nav graph route
            val viewModel: NewContViewModel = hiltViewModel(parentEntry)
            ContactsScreen(
                viewModel = viewModel,
                onAddContact = {
                    navController.navigate(ContactsActionsScreen.CreateContact()) {
                        popUpTo(BottomBarScreen.Contacts.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onClickContact = { contact: ContactEntity ->
                    navController.navigate(ContactsActionsScreen.ContactInfo(contact)) {
                        popUpTo(BottomBarScreen.Contacts.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onLoginAgain = logOut
            )
        }
        composable(route = BottomBarScreen.Notifications.route){
            val parentEntry = remember(it) { navController.getBackStackEntry(BottomBarScreen.Notifications.route) } // your nav graph route
            val viewModel: NotifViewModel = hiltViewModel(parentEntry)
            NotificationsScreen(
                notifViewModel = viewModel,
                logInAgain = logOut
            )
        }
        settingNavGraph(
            navController = navController,
            logOut = logOut
        )
        contactsActionNavGraph(
            navController = navController,
            onSaveAndContinue = {
                navController.navigate(BottomBarScreen.Contacts.route) {
                    popUpTo(BottomBarScreen.Contacts.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onLoginAgain = logOut
        )
        meetingActionNavGraph(
            navController = navController,
            logOut = logOut,
            onCancelMeeting = {
                navController.navigate(BottomBarScreen.Home.route) {
                    popUpTo(BottomBarScreen.Home.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            goMeetingScreen = {
                navController.navigate(BottomBarScreen.Meetings.route) {
                    popUpTo(BottomBarScreen.Meetings.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onEditMeeting = {
                navController.navigate(MeetingsActionsScreen.MeetingDays(it)){
                    popUpTo(BottomBarScreen.Meetings.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}

/**
 * ✅ You can only popUpTo(...) a navigation destination that:
 * ✅ Is a sealed object (like data object)
 *
 * ✅ Has no parameters
 *
 * ✅ Represents a real destination route (like BottomBarScreen.Contacts or ContactsActionsScreen.CreateContact)
 *
 *
 */