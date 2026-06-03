package com.example.meerkat.feature_notification.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.meerkat.R
import com.example.meerkat.core.calendar.domain.model.SessionExpiredException
import com.example.meerkat.core.presentation.NoEntries
import com.example.meerkat.core.presentation.SessionExpiredDialog
import com.example.meerkat.feature_notification.domain.model.NotificationEvent

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    paging: LazyPagingItems<NotificationEvent>,
    dialogState : Boolean,
    showDialog : () -> Unit ,
    onDismiss : () -> Unit,
    onLogInAgain : () -> Unit
) {
    if(dialogState) {
        SessionExpiredDialog(
            onDismiss = onDismiss,
            onLoginAgain = onLogInAgain
        )
    }
    Box(modifier = modifier.padding(horizontal = 5.dp).fillMaxSize()) {

        when (val state = paging.loadState.refresh) {
            is LoadState.Loading -> {
                println("paging Loading State  refresh is loading")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                println("paging refresh error = ${state.error}")
                if(state.error is SessionExpiredException){
                    println("session has expired")
                    showDialog()
                }
                RetryButton { paging.retry() }
            }

            is LoadState.NotLoading -> {
                if (paging.itemCount == 0) {
                    println("No entries found")
                    NoEntries(
                        modifier = Modifier,
                        image = R.drawable.notifications,
                        txt1 = "No Notifications",
                        txt2 = "Notifications will appear here when Calendly events are scheduled or updated"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                    ) {
                        item {
                            if (paging.loadState.prepend is LoadState.Loading) {
                                Box(
                                    Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else if (paging.loadState.prepend is LoadState.Error) {
                                RetryButton { paging.retry() }
                            }
                        }

                        items(
                            count = paging.itemCount,
                            key = paging.itemKey { it.id },
                            contentType = paging.itemContentType { "contentType" },
                        ) { index ->
                            paging[index]?.let { notificationEvent ->
                                NotificationItem(
                                    notificationEvent = notificationEvent,
                                    onViewContactClick = {
                                        // Handle click
                                    }
                                )
                            }
                        }

                        item {
                            when (paging.loadState.append) {
                                is LoadState.Loading -> {
                                    Box(
                                        Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                is LoadState.Error -> {
                                    RetryButton { paging.retry() }
                                }

                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}
