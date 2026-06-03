package com.example.meerkat.feature_meetings.domain.model

data class EmailMessage(
    val email : String ,
    val message: Map<String, String>
)
