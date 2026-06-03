package com.example.meerkat.feature_setting.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.core.presentation.DefaultImage
import com.example.meerkat.ui.theme.blue400


@Composable
fun UserProfile(
    logOut: () -> Unit
) {
    Column(
        modifier = Modifier.padding(start = 30.dp , top = 30.dp)
    ) {
        DefaultImage(
            modifier = Modifier
                .size(70.dp),
            onPickImage = {}
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Maham",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "https:\\calendly.com/mahamsameenhere")
            Spacer(Modifier.width(10.dp))
            Icon(
                painter = painterResource(R.drawable.baseline_content_copy_24),
                contentDescription = "",
                tint = blue400
            )
        }
        Spacer(Modifier.height(20.dp))
    }
    UtilText( text = "Availability")
    UtilText( text = "Default Messages")
    UtilText( text = "Notifications")
    UtilText( text = "Labs")
    UtilText( text = "Help and Support")
    UtilText( text = "Send feedback")
    UtilText(
        text = "Logout" ,
        onClick = logOut
    )
    HorizontalDivider()
    Column (
        modifier = Modifier.padding(start = 30.dp)
    ){
        Spacer(Modifier.height(10.dp))
        Text(text = "VERSION   2.27.0")
        Spacer(Modifier.height(10.dp))
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )) {
                append("Easy")
            }
            append("   ")
            withStyle(style = SpanStyle(
                color = blue400 ,fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )) {
                append("ahead")
            }
        })
    }
}
