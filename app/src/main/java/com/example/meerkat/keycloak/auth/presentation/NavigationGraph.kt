package com.example.meerkat.keycloak.auth.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable


@Composable
fun NavigationGraph(
    navController : NavHostController ,
){
    NavHost(
        navController = navController ,
        startDestination = KeycloakScreen.ScreenA
    ){
        composable <KeycloakScreen.ScreenA>(
            deepLinks = listOf(
                navDeepLink<KeycloakScreen.ScreenA>(
                    basePath = "https://saadmirza.net/redirect"
                )
            )
        ){
            val viewModel = hiltViewModel<ViewModel>()
            AuthWithKeyCloak(viewModel)
        }
    }
}


sealed class KeycloakScreen{
    @Serializable
    data object ScreenA : KeycloakScreen()
}