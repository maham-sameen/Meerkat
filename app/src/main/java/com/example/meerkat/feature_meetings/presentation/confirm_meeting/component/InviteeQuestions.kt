package com.example.meerkat.feature_meetings.presentation.confirm_meeting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun InviteeQuestions(
    addNote : () -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.Center ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text =  "Invitee questions",
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold ,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Optional",
                fontSize = 15.sp ,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        MeetingPrepCard(
            addNote = addNote
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}


@Composable
fun MeetingPrepCard(
    addNote: () -> Unit
) {
    Row (
        modifier = Modifier.fillMaxWidth() ,
        horizontalArrangement = Arrangement.SpaceBetween ,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), // light gray background
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .weight(1f) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Please share anything that will help prepare for our meeting.",
                modifier = Modifier
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Checkmark",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
                .clickable{
                    addNote()
                }
        )
    }
}
