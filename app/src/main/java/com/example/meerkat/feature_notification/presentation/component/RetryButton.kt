package com.example.meerkat.feature_notification.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RetryButton(onClick : () -> Unit) {
    Button(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = "Retry",
            tint = MaterialTheme.colorScheme.onPrimary // this will ensure it's visible
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Retry",
            color = MaterialTheme.colorScheme.onPrimary // ensure text is readable
        )
    }
}