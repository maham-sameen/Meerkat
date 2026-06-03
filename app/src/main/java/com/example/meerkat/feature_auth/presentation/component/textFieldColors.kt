package com.example.meerkat.feature_auth.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import com.example.meerkat.ui.theme.blue400
import com.example.meerkat.ui.theme.blue800

@Composable
fun getTextFieldColors():TextFieldColors{
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedLabelColor = blue400,
        unfocusedLabelColor = blue800,
        focusedBorderColor = blue400,
        unfocusedBorderColor = blue800,
        cursorColor = blue400
    )
}