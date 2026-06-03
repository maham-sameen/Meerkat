package com.example.meerkat.feature_setting.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun UtilText(
    text : String ,
    onClick : () -> Unit = {}
) {
    Column (
        modifier = Modifier.clickable(
            onClick = onClick
        )
    ){
        HorizontalDivider()
        Spacer(Modifier.height(20.dp))
        Text(
            text = text,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(Modifier.height(20.dp))
    }
}