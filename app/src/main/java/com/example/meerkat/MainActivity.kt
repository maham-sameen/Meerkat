package com.example.meerkat


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.meerkat.core.RootNavigationGraph
import com.example.meerkat.core.presentation.component.LoadingAnimation
import com.example.meerkat.core.util.Graph
import com.example.meerkat.core.util.SessionManager
import com.example.meerkat.ui.theme.MeerkatTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


val fontFamily = FontFamily(
    Font(R.font.quicksand_bold , FontWeight.Bold) ,
    Font(R.font.quicksand_light , FontWeight.Light),
    Font(R.font.quicksand_medium  , FontWeight.Medium ) ,
    Font(R.font.quicksand_regular , FontWeight.W100),
    Font(R.font.quicksand_semibold , FontWeight.SemiBold)
)


val LocalFontFamily = staticCompositionLocalOf { fontFamily }


/**
 * The email extension in the firestore that is below
 * https://medium.com/firebase-tips-tricks/how-to-install-and-use-trigger-email-extension-from-firebase-f6f9763c8088
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController


    @Inject
    lateinit var sessionManager : SessionManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            MeerkatTheme {
                Timber.plant(Timber.DebugTree())
                navController = rememberNavController()
                var isChecking by remember { mutableStateOf(true) }
                var isLoggedIn by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    isLoggedIn = sessionManager.isUserLoggedIn()
                    isChecking = false
                }
                if (isChecking) {
                    //✅ Show your custom loading screen *alone*
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingAnimation()
                    }
                } else {
                    //✅ Only render navigation graph once login check is done
                    RootNavigationGraph(
                        navController = navController,
                        startDestination = if (isLoggedIn) Graph.Home else Graph.Authentication
                    )
                }
            }
        }
    }
}




/*
navController = rememberNavController()
                var isChecking by remember { mutableStateOf(true) }
                var isLoggedIn by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    isLoggedIn = sessionManager.isUserLoggedIn()
                    isChecking = false
                }
                if (isChecking) {
                    //✅ Show your custom loading screen *alone*
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingAnimation()
                    }
                } else {
                    //✅ Only render navigation graph once login check is done
                    RootNavigationGraph(
                        navController = navController,
                        startDestination = if (isLoggedIn) Graph.Home else Graph.Authentication
                    )
                }
*/




/*
https://learn.microsoft.com/en-us/entra/identity-platform/tutorial-v2-android

https://stackoverflow.com/questions/57353879/error-intent-filter-for-browsertabactivity-is-missing-while-using-azuread-ms
 */