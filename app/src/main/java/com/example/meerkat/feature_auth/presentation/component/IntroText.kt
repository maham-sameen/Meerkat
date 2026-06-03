package com.example.meerkat.feature_auth.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.core.presentation.component.MainTextApp

@Composable
fun IntroText(
    introText : String = "Log into Your Calendly Account"
) {
    Spacer(Modifier.height(40.dp))
    MainTextApp(
        fontWeight = FontWeight.Bold ,
        fonSize = 50.sp ,
        iconSize = 45.dp
    )
    Spacer(Modifier.height(20.dp))
    Text(
        text = introText ,
        fontSize = 20.sp ,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(40.dp))
}