package com.example.meerkat.core.util

import kotlinx.serialization.Serializable

sealed class Graph {

    @Serializable
    data object Authentication : Graph()

    @Serializable
    data object Home : Graph()

    @Serializable
    data object Settings : Graph()

    @Serializable
    data object ContactActions : Graph()

    @Serializable
    data object MeetingActions : Graph()
}