package com.example.meerkat.feature_contacts.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun DetailComp(
    iconModifier : Modifier = Modifier.size(30.dp),
    imageVector : ImageVector? = null,
    @DrawableRes
     painter : Int? = null,
    field : @Composable (() -> Unit),
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center ,
        verticalAlignment = Alignment.CenterVertically
    ){
        if(imageVector != null){
            Icon(
                imageVector = imageVector,
                contentDescription = "",
                modifier = iconModifier ,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }else{
            Icon(
                painter = painterResource(painter!!),
                contentDescription = "",
                modifier = iconModifier ,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.width(10.dp))
        field()
    }
}
