package com.example.meerkat.feature_notification.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.meerkat.core.presentation.topAppBarColors
import com.example.meerkat.ui.theme.blue400
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp


@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun EventDetailsScreenPreview() {
    EventDetailsScreen()
}

@Composable
fun EventDetailsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { TopAppBarEventDetails() }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopAppBarEventDetails() {
    TopAppBar(
        title = {} ,
        navigationIcon = {
            Box(contentAlignment = Alignment.Center , modifier = Modifier.fillMaxHeight()){
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = "previous screen",
                    modifier = Modifier.size(50.dp),
                    tint = blue400
                )
            }
        } ,
        colors = topAppBarColors(),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
}



@Preview
@Composable
fun MeetingDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Maham and Maham",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Mon, June 30, 2025\n12:30 PM - 1:00 PM (GMT+05:00)",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Meeting details",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "https://calendly.com/events/aae61efe-29...",
            color = Color(0xFF64B5F6),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "30 Minute Meeting",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Invitee details",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Maham",
            color = Color(0xFF64B5F6),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Scheduled",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "mahamsameenhere@gmail.com",
            color = Color(0xFF64B5F6),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Pakistan Standard Time",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* Handle mark no-show */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Mark no-show", color = Color.White)
            }

            Button(
                onClick = { /* Handle book follow-up */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Book follow-up", color = Color.White)
            }
        }
    }
}
