package com.example.meerkat.feature_contacts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumber(
    @SerialName("country_code")val countryCode : String,
    @SerialName("phone_number")val phoneNumber : String
)
