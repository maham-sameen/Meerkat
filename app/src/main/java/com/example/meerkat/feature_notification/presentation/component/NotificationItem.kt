package com.example.meerkat.feature_notification.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.feature_notification.domain.model.NotificationEvent
import com.example.meerkat.ui.theme.blue400


@Composable
fun NotificationItem(
    notificationEvent: NotificationEvent,
    onViewContactClick: () -> Unit
) {
    val fontSize = 18.sp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 10.dp , end = 10.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_event_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "New Event Scheduled",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface ,
                    fontSize = fontSize
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = notificationEvent.timeAgo,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface ,
                    fontSize = fontSize
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Start)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(notificationEvent.organizer ?: "")
                        }
                        append(" scheduled ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(notificationEvent.meetingTitle)
                        }
                        append(" with ")
                        notificationEvent.meetingWith.forEachIndexed { index: Int, it: String ->
                            if (index == notificationEvent.meetingWith.size - 1) {
                                append(it)
                            } else {
                                append("$it , ")
                            }
                        }
                        append(" on ")
                        append(notificationEvent.meetingDateTime)
                        append(", ${notificationEvent.timeZoneName}")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface ,
                fontSize = fontSize ,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onViewContactClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = blue400.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    "View Contact" ,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
    }
}
