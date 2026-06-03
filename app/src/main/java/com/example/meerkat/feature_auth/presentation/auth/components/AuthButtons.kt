package com.example.meerkat.feature_auth.presentation.auth.components
import androidx.annotation.DrawableRes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AuthButton(
    @DrawableRes
    icon : Int ,
    text : String ,
    modifier: Modifier,
    onClickLabel : String ,
    onClick : () -> Unit
){
    OutlinedButton(
        onClick = onClick ,
        modifier = modifier
            .semantics{
                contentDescription = onClickLabel
            }
        ,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onSurface  ,
            containerColor = Color.Transparent ,
        ) ,
        border = BorderStroke(1.dp , MaterialTheme.colorScheme.secondary)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(
                painter = painterResource(icon) ,
                contentDescription = "sign up  with google account" ,
                tint = Color.Unspecified ,
                modifier = Modifier.size(25.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text ,
                fontSize = 15.sp
            )
        }
    }
}