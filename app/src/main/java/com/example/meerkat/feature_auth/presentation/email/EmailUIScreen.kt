package com.example.meerkat.feature_auth.presentation.email

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meerkat.core.presentation.DisableSoftKeyboard
import com.example.meerkat.core.presentation.component.LoadingAnimation
import com.example.meerkat.core.presentation.component.showSnackBarUI
import com.example.meerkat.feature_auth.presentation.component.IntroText
import com.example.meerkat.core.presentation.ShowUIState
import com.example.meerkat.core.presentation.UIState
import com.example.meerkat.feature_auth.presentation.email.components.EmailMainCompScreen
import kotlinx.coroutines.delay




@Composable
fun EmailUIScreen(
    emailViewModel: EmailViewModel
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val UIState = emailViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UIState.Idle,
    )
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.surface
        ) {
            DisableSoftKeyboard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    IntroText(emailViewModel.introTxt.value)
                    EmailMainCompScreen(emailViewModel)
                    Spacer(Modifier.height(40.dp))
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
                                actionLabel = null,
                                onDismiss = {},
                                onActionPerformed = {}
                            )
                            delay(1000)
                            emailViewModel.clearUiState()
                        }
                    },
                    noAccount = {},
                    onSuccess = {},
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EmailUIScreenPreview() {
    val emailViewModel = hiltViewModel<EmailViewModel>()
    EmailUIScreen(emailViewModel)
}