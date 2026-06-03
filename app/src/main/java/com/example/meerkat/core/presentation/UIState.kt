package com.example.meerkat.core.presentation

import android.content.Intent


sealed class UIState {
    object Idle : UIState()
    object Loading : UIState()
    data class Success(val message: String? = null) : UIState()
    data class Error(val message: String) : UIState()
    data class NoAccount(val intent : Intent): UIState()
}