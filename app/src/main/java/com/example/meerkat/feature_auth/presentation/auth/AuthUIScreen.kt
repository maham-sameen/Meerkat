package com.example.meerkat.feature_auth.presentation.auth

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meerkat.core.presentation.component.LoadingAnimation
import com.example.meerkat.core.presentation.findActivity
import com.example.meerkat.core.presentation.component.showSnackBarUI
import com.example.meerkat.MainActivity
import com.example.meerkat.feature_auth.presentation.component.IntroText
import com.example.meerkat.feature_auth.presentation.intro.CurrentScreen
import com.example.meerkat.core.presentation.ShowUIState
import com.example.meerkat.core.presentation.UIState
import com.example.meerkat.feature_auth.presentation.AuthViewModel
import com.example.meerkat.feature_auth.presentation.auth.components.Authentication
import com.example.meerkat.feature_auth.presentation.auth.components.TransparentErrorMessageTextField
import kotlinx.coroutines.delay


@Preview(showBackground = true)
@Preview("dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_C, showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthUIScreen(
        onAction = {},
        authWithEmail = {
            currentScreen : CurrentScreen , value : String ->
        } ,
       goHome =  {}
    )
}


@Composable
fun AuthUIScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onAction : (CurrentScreen) -> Unit,
    authWithEmail : (CurrentScreen , String) -> Unit ,
    goHome : () -> Unit
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current
    val activity =  context.findActivity()
    val UIState = authViewModel.uiState.collectAsStateWithLifecycle()

    val authorizationLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                println("authorizationLauncher : ${result.data!!}")
                result.data?.let { data ->
                    authViewModel.authScreenEvent(
                        AuthEvents.HandleAuth(
                            intent = data
                        )
                    )
                }
            }
        }

    val isLoggedIn =  authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn.value){
            if (authViewModel.isLoggedIn.value) {
                goHome()
            }
        }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {}
    ) { innerPadding ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally ,
                verticalArrangement = Arrangement.Center ,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ){
                IntroText(authViewModel.authLangState.value.enterIntoCalendlyAccount)
                Authentication(
                    authLangState = authViewModel.authLangState.value,
                    onContinueEmailAddress = {
                        println("\uD83D\uDE00 onContinue Email Address \n  " +
                                "currentScreenValue : ${authViewModel.currentScreen.value} \n" +
                                "emailAddress : ${authViewModel.emailAddress.value}")
                        authWithEmail(
                            authViewModel.currentScreen.value,
                            authViewModel.emailAddress.value.text
                        )
                    },
                    getStartedWithGoogleAccount = {
                        authViewModel.authScreenEvent(
                            AuthEvents.StartWithGoogle(
                                context = context
                            )
                        )
                    },
                    getStartedWithMicrosoftAccount = {
                        authViewModel.authScreenEvent(AuthEvents.StartWithMicrosoft(activity = activity as MainActivity))
                    },
                    onAction = {
                        onAction(authViewModel.currentScreen.value)
                    },
                    transparentErrorMessageTextFilled = {
                        TransparentErrorMessageTextField(
                            errorMessageTextFieldState = authViewModel.emailAddress.value,
                            modifier = Modifier,
                            onValueChange = {
                                authViewModel.authScreenEvent(
                                    AuthEvents.EnterEmailAddress(
                                        it
                                    )
                                )
                            },
                            hint = "email address",
                            isValidEmail = { authViewModel.authScreenEvent(AuthEvents.EmailAddressValidation) },
                        )
                    }
                )
                Spacer(Modifier.height(100.dp))
            }
            ShowUIState(
                uiState = UIState.value,
                showLoading = {
                    LoadingAnimation()
                },
                showMessage = { message ->
                    LaunchedEffect(message) {
                        showSnackBarUI(
                            snackBarHostState = snackBarHostState,
                            message = message,
                            actionLabel = "",
                            onDismiss = {},
                            onActionPerformed = {}
                        )
                        delay(1000)
                        authViewModel.clearUiState()
                    }
                },
                noAccount = { intent ->
                    authorizationLauncher.launch(intent)
                },
                onSuccess = {},
            )
        }
    }
}

