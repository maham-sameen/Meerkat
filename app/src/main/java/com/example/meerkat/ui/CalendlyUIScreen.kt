package com.example.meerkat.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.meerkat.Constants
import com.example.meerkat.feature_event.presentation.util.BottomBarScreen
import com.example.meerkat.feature_event.presentation.HomeNavGraph
import com.example.meerkat.ui.theme.blue400

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendlyUIScreen(
    logOut : () -> Unit
) {
    val navController = rememberNavController()
    //var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Meetings,
        BottomBarScreen.Contacts,
        BottomBarScreen.Notifications
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = Constants.screenRoutes.any { it == currentDestination?.route }
    Scaffold(
        bottomBar = {
            if(bottomBarDestination) {
                BottomBar(
                    screens = screens ,
                    selected = {  screen ->
                        currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true } ,
                    onClick = {screen ->
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.surface
            ) {
            HomeNavGraph(
                navController = navController,
                logOut = logOut
            )
        }
    }
}


@Composable
fun BottomBar(
    screens : List<BottomBarScreen>,
    selected : (BottomBarScreen) -> Boolean,
    onClick : (BottomBarScreen) -> Unit
) {
        NavigationBar(
            content = {
                screens.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selected(item),
                        onClick = {
                            onClick(item)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    when {
                                        item.badgeCount != null -> Badge { Text(text = item.badgeCount.toString()) }
                                        item.hasNews -> Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(
                                        when {
                                            item is BottomBarScreen.Home -> {
                                                if (selected(item)) item.selectedIcon else item.unSelectedIcon!!
                                            }

                                            else -> item.selectedIcon
                                        }
                                    ),
                                    contentDescription = item.title,
                                    tint = when {
                                        item is BottomBarScreen.Home && selected(item) -> Color.Unspecified
                                        selected(item) -> blue400
                                        else -> MaterialTheme.colorScheme.onSecondaryContainer
                                    },
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item.title, color = when {
                                    selected(item) -> blue400
                                    else -> MaterialTheme.colorScheme.onSecondaryContainer
                                }
                            )
                        }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )

}

