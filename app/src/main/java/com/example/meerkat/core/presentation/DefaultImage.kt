package com.example.meerkat.core.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.meerkat.R
import com.example.meerkat.ui.theme.Blue07
import com.example.meerkat.ui.theme.PurpleGrey40


@Composable
fun DefaultImage(
    modifier: Modifier,
    onPickImage : () -> Unit
) {
    Card (
        modifier = modifier
            .clickable {
                onPickImage()
            }
        ,
        shape = CircleShape ,
        border = BorderStroke(2.dp , Color.White) ,
        colors = CardDefaults.cardColors(
            containerColor = Blue07,
            contentColor = PurpleGrey40
        )
    ){
        Icon(
            painter = painterResource(R.drawable.baseline_person_24) ,
            contentDescription = null ,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .clickable {
                    onPickImage()
                }
        )
    }
}