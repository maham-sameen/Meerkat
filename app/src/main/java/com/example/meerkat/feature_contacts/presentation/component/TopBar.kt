package com.example.meerkat.feature_contacts.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.core.presentation.topAppBarColors
import com.example.meerkat.ui.theme.blue400


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNewContactsBar(
    isSaveContinue : Boolean = false ,
    onBackPressed : () -> Unit ,
    onSaveAndContinue : () -> Unit
) {
    TopAppBar(
        title = {} ,
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft ,
                contentDescription = "" ,
                tint = blue400 ,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
                    .clickable{
                        onBackPressed()
                    }
            )
        },
        actions = {
            Text(
                text = "Save and continue" ,
                fontSize = 15.sp ,
                modifier = Modifier.padding(end = 10.dp).clickable{
                    onSaveAndContinue()
                },
                color = if(isSaveContinue) blue400 else Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
    )
}