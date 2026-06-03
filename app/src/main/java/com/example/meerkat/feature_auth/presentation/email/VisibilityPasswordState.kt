package com.example.meerkat.feature_auth.presentation.email

import androidx.annotation.DrawableRes
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.meerkat.R

data class VisibilityPasswordState(
    val password: String  = "",
    val visualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val visibility: Boolean = false,
    @DrawableRes
    val painter: Int = R.drawable.baseline_visibility_24
)