package com.example.meerkat.feature_notification.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.meerkat.feature_notification.presentation.component.MainContent
import com.example.meerkat.core.presentation.topAppBarColors

@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun NotificationScreenPreview() {
    //NotificationsScreen()
}


@Composable
fun NotificationsScreen(
    notifViewModel: NotifViewModel,
    logInAgain : () -> Unit
) {
    Scaffold (
        topBar = {
            TopNotificationsBar()
        }
    ){ innerPadding ->

        val paging = notifViewModel.notificationEvents.collectAsLazyPagingItems()
        MainContent(
            modifier = Modifier.padding(innerPadding),
            paging = paging,
            dialogState = notifViewModel.sessionDialogState.value,
            onDismiss = { notifViewModel.onEvent(NotificationsEvents.DismissDialog) },
            onLogInAgain = logInAgain,
            showDialog = { notifViewModel.onEvent(NotificationsEvents.ShowDialog) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNotificationsBar() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxHeight() ,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 20.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = topAppBarColors(),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
}
