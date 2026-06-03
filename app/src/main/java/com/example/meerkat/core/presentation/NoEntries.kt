package com.example.meerkat.core.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun NoEntries(
    modifier: Modifier,
    @DrawableRes
    image : Int ,
    txt1 : String ,
    txt2 : String
) {
    Column (
        modifier = modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        HorizontalDivider()
        Image(
            painter = painterResource(image),
            contentDescription = "" ,
            modifier = Modifier.size(400.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = txt1 ,
            fontSize = 18.sp ,
            fontWeight = FontWeight.Bold ,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = txt2,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                top = 10.dp,
                start = 30.dp,
                end = 20.dp
            ),
            lineHeight = 25.sp,
            textAlign = TextAlign.Center
        )
    }
}