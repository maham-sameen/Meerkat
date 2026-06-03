package com.example.meerkat.feature_setting.presentation

import kotlinx.serialization.Serializable

sealed class Settings {
    @Serializable
    data object Profile : Settings()
}