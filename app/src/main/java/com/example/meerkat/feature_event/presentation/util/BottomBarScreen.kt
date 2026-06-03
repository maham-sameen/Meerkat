package com.example.meerkat.feature_event.presentation.util

import androidx.annotation.DrawableRes
import com.example.meerkat.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomBarScreen (
    val title : String ,
    @DrawableRes
    val selectedIcon : Int,
    @DrawableRes
    val unSelectedIcon : Int?  = null,
    val hasNews : Boolean = false ,
    val badgeCount: Int? = null ,
    val  route : String
){

    @Serializable
    object Home : BottomBarScreen(
        title = "Home",
        selectedIcon = R.drawable.darklogo ,
        unSelectedIcon = R.drawable.lightcalendly ,
        route = "home"
    )

    @Serializable
    object Meetings : BottomBarScreen(
        title = "Meetings",
        selectedIcon = R.drawable.baseline_calendar_today_24 ,
        unSelectedIcon = null ,
        route = "meetings"
    )

    @Serializable
    object Contacts : BottomBarScreen(
        title = "Contacts",
        selectedIcon = R.drawable.baseline_people_outline_24,
        unSelectedIcon = null ,
        route = "contacts"
    )

    @Serializable
    object Notifications : BottomBarScreen(
        title = "Notifications",
        selectedIcon = R.drawable.baseline_notifications_none_24 ,
        unSelectedIcon = null ,
        route = "notifications"
    )
}