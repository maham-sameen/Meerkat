package com.example.meerkat.feature_contacts.presentation

import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import kotlinx.serialization.Serializable

@Serializable
sealed class ContactsActionsScreen {


    @Serializable
    data class CreateContact(val contactEntity: ContactEntity? = null) : ContactsActionsScreen()

    @Serializable
    data class ContactInfo(val contactEntity: ContactEntity) : ContactsActionsScreen()
}