package com.example.meerkat.feature_meetings.data.data_source.model.remote

data class UserInfoDto(
    val email: String,
    val name: String?,         // optional
    val picture: String?,      // optional
    val sub: String?           // user id
)
