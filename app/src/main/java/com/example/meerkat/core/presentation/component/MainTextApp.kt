package com.example.meerkat.core.presentation.component

import android.app.UiModeManager
import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.LocalFontFamily
import com.example.meerkat.ui.theme.blue400
import com.example.meerkat.R


@Preview(showBackground = true)



@Composable
fun MainTextApp(modifier: Modifier = Modifier ,
                fontWeight : FontWeight = FontWeight.Normal ,
                fonSize : TextUnit= 25.sp ,
                iconSize : Dp = 30.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(R.drawable.darklogo) ,
            contentDescription = null ,
            modifier = Modifier.size(iconSize) ,
            tint = Color.Unspecified
        )
        Spacer(Modifier.width(7.dp))
        Text(
            text = "Calendly",
            modifier = modifier,
            fontSize = fonSize,
            fontFamily = LocalFontFamily.current,
            fontWeight = fontWeight,
            color = blue400 // ✅ Uses primary theme color
        )
    }
}


fun isDarKMode(context: Context): Boolean{
    val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    val  mode = uiModeManager.nightMode
    return mode == UiModeManager.MODE_NIGHT_YES
}