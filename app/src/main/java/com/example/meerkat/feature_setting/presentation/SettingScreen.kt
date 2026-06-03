package com.example.meerkat.feature_setting.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.meerkat.core.presentation.topAppBarColors
import com.example.meerkat.feature_setting.presentation.component.UserProfile
import com.example.meerkat.ui.theme.blue400


@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun SettingScreensPreview() {
    SettingScreens({})
}



@Composable
fun SettingScreens( logOut : () -> Unit) {
    Scaffold(
        topBar = {
            TopSettingBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalDivider()
            UserProfile(
                logOut  = logOut
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSettingBar() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxHeight() ,
                contentAlignment = Alignment.Center
            ){ Text(
                text = "Settings" ,
                fontWeight = FontWeight.Bold
            ) }
        },
        navigationIcon = {
            Box(
                modifier = Modifier.fillMaxHeight() ,
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp) ,
                    tint  = blue400
                )
            }
        },
        colors = topAppBarColors(),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
}
