package com.example.meerkat.core.presentation

import android.content.Intent
import androidx.compose.runtime.Composable

@Composable
fun ShowUIState(
    uiState: UIState,
    showLoading: @Composable () -> Unit,
    showMessage: @Composable (String) -> Unit,
    noAccount: (intent : Intent) -> Unit,
    onSuccess: @Composable () -> Unit,
) {

    when (uiState) {

        is UIState.Loading -> {
            showLoading()
        }

        is UIState.Error -> {
            showMessage(uiState.message)
        }

        is UIState.Success -> {
            uiState.message?.let { showMessage(it) }
            onSuccess()
        }

        UIState.Idle -> Unit

        is UIState.NoAccount -> {
            noAccount(uiState.intent)
        }
    }
}