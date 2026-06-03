package com.example.meerkat.core.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.ui.theme.blue400


@Composable
fun AddNewEvent(
    @DrawableRes
    icon : Int ,
    modifier: Modifier = Modifier.padding(top = 15.dp , end = 15.dp , bottom = 15.dp),
    eventText : String ,
    textColor : Color =  MaterialTheme.colorScheme.onSurface ,
    iconColor : Color = blue400 ,
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(modifier)
        ,
        verticalAlignment = Alignment.Top ,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null  ,
            tint = iconColor ,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Top)
        )
        Spacer(Modifier.width(7.dp))
        Text(
            text = eventText ,
            fontSize = 16.sp ,
            color = textColor ,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}
/**
 * But in Row, Alignment.Top doesn't push the children up unless some child has height > others. Since:
 *
 * Icon and Text are similar height
 *
 * There’s no child stretching vertically
 *
 * ➡️ Compose treats everything as aligned by baseline or center visually.
 */