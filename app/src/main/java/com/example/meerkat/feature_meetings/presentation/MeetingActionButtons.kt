package com.example.meerkat.feature_meetings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meerkat.ui.theme.blue400


@Composable
fun MeetingActionButton(
    modifier: Modifier = Modifier ,
    btnTextOne : String ,
    onBtnTextOne : () -> Unit ,
    btnTextTwo : String ,
    onBtnTextTwo : () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onBtnTextOne,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = blue400.copy(0.2f))
        ) {
            Text(btnTextOne, color = blue400)
        }

        Button(
            onClick = onBtnTextTwo,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor =  blue400)
        ) {
            Text(btnTextTwo, color = Color.White)
        }
    }
}
