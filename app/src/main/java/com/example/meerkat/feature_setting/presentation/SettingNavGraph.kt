package com.example.meerkat.feature_setting.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.meerkat.core.util.Graph

fun NavGraphBuilder.settingNavGraph(
    navController : NavHostController ,
    logOut : () -> Unit
) {
    navigation<Graph.Settings>(startDestination = Settings.Profile){
        composable<Settings.Profile> {
            SettingScreens(
                logOut = logOut
            )
        }
    }
}