package com.example.meerkat.core

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.meerkat.core.util.Graph
import com.example.meerkat.feature_auth.presentation.authNavigation
import com.example.meerkat.ui.CalendlyUIScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavigationGraph(
    navController : NavHostController ,
    startDestination : Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        authNavigation(
           navController =  navController,
            goHome = {
                navController.navigate(Graph.Home){
                    popUpTo(Graph.Home){
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
        composable <Graph.Home>(
            deepLinks = listOf(
                navDeepLink<Graph.Home>(
                    basePath = "https://saadmirza.net/Meerkat/home"
                )
            )
        ){
            CalendlyUIScreen(
                logOut = {
                    navController.navigate(Graph.Authentication){
                        popUpTo(Graph.Authentication){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }


//        composable(
//            route = "home" ,
//            deepLinks = listOf(
//                navDeepLink {
//                    uriPattern = "https://saadmirza.net/home"
//                    action = Intent.ACTION_VIEW
//                }
//            )
//        ) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = "Home")
//            }
//        }
        composable(
            route = "detail",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://saadmirza.net/Meerkat/{id}"
                    action = Intent.ACTION_VIEW
                }
            ),
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("id")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "The id is $id")
            }
        }
        composable(
            route = "privacy",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://saadmirza.net/Meerkat/privacy-policy"
                    action = Intent.ACTION_VIEW
                }
            )
        ) {
            /* PrivacyScreen() */
            Box(
                modifier = Modifier.fillMaxSize() ,
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Privacy Screen"
                )
            }
        }

        composable(
            route = "terms",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://saadmirza.net/Meerkat/terms"
                    action = Intent.ACTION_VIEW
                }
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize() ,
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Service Term"
                )
            }
        }

        composable(
            route = "schedule/{username}/{slot}",
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                    defaultValue = "Maham Sameen"
                },
                navArgument("slot") {
                    type = NavType.StringType
                    defaultValue = "30 min meetings"
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://saadmirza.net/Meerkat/{username}/{slot}"
                    action = Intent.ACTION_VIEW
                }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val slot = backStackEntry.arguments?.getString("slot") ?: ""

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Username: $username\nSlot: $slot",
                    fontSize = 24.sp
                )
            }
        }
    }
}