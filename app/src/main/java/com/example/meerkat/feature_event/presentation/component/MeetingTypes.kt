package com.example.meerkat.feature_event.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.feature_contacts.presentation.component.ContactLogo
import com.example.meerkat.ui.theme.blue800



@Preview(showBackground = true)
@Composable
fun EventType() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Meeting types",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold ,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().clickable{} ,
            shadowElevation = 4.dp ,
            tonalElevation = 4.dp ,
            color = MaterialTheme.colorScheme.surface ,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            EventTypeItem()
        }
    }
}
@Composable
fun EventTypeItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) , // ✅ this makes the Row height match its tallest child ,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Left Vertical Indicator
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .fillMaxHeight()
                .background(
                    Color(0xFF6D3FE7),
                    RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50)
                )
        )
        Spacer(modifier = Modifier.width(12.dp))

        // Text Content
        Column(
            modifier = Modifier.weight(1f) // Allows text to take up remaining space
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "30 Minute Meeting",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "One-on-one, 30 mins",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
    }
}
