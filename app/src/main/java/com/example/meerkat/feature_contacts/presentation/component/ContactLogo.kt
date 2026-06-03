package com.example.meerkat.feature_contacts.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun ContactLogo(modifier: Modifier = Modifier, firstCapitalLetterTxt : @Composable () -> Unit ) {
    Surface(
        modifier =  modifier ,
        shape = CircleShape ,
        color = MaterialTheme.colorScheme.surfaceVariant
    ){
        Box(modifier = Modifier
            .fillMaxSize(1.0f), // it will fill parent box , // padding will help us to give some margin between our text and parent if text greater then our parent size
            contentAlignment = Alignment.Center
        ){
            firstCapitalLetterTxt()
        }
    }
}
