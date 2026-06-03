package com.example.meerkat.feature_contacts.domain.use_case

import com.example.meerkat.core.time_zone.domain.use_case.GetTimeZonesUseCase

data class ContactUseCases(
    val insertNewContact: InsertNewContact ,
    val getTimeZonesUseCase: GetTimeZonesUseCase ,
    val getAllContacts: GetAllContacts ,
    val getCurrentUserId: GetCurrentUserId
)
