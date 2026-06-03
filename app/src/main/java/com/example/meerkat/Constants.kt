package com.example.meerkat

import com.example.meerkat.feature_event.presentation.util.BottomBarScreen


object Constants {

    val screenRoutes = listOf(
        BottomBarScreen.Home.route,
        BottomBarScreen.Meetings.route,
        BottomBarScreen.Contacts.route,
        BottomBarScreen.Notifications.route
    )
    val appName = "MeerkatApp"
}

