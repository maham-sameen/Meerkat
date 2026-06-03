package com.example.meerkat.core.time_zone.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import com.example.meerkat.ui.theme.blue400

@Composable
fun TimeZonesItem(
    timeZoneInfo: TimeZoneInfo,
    selectedOption: TimeZoneInfo,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 50.dp)
                .fillMaxWidth()
                .selectable(
                    selected = (timeZoneInfo.name == selectedOption.name),
                    onClick = onClick,
                    role = Role.RadioButton
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = timeZoneInfo.name,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f) // Ensures text takes available space
            )
            Spacer(modifier = Modifier.width(8.dp)) // Ensures spacing before RadioButton
            Box(modifier = Modifier.size(30.dp)) { // Fixed size to keep alignment
                RadioButton(
                    selected = (timeZoneInfo.name == selectedOption.name),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        selectedColor = blue400
                    )
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    }
}
