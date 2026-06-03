package com.example.meerkat.feature_notification.presentation

sealed class NotificationsEvents {
    data object DismissDialog : NotificationsEvents()
    data object ShowDialog : NotificationsEvents()
}