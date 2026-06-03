package com.example.meerkat.feature_contacts.presentation

import androidx.compose.ui.focus.FocusState
import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.togitech.ccp.data.PhoneCode

sealed class NewContactEvents {
    data class EnterName(
        val name : String
    ): NewContactEvents()
    data class EnterEmail(
        val email : String
    ): NewContactEvents()
    data class EnterPhoneNo(
        val phoneNo : String ,
        val isValid : Boolean ,
        val code : PhoneCode
    ): NewContactEvents()
    data class EnterTimeZone(
        val text : String
    ): NewContactEvents()
    data class FocusChangePhoneNoTxtField(
        val focusState: FocusState
    ): NewContactEvents()
    data class FocusChangeEmailTxtField(
        val focusState: FocusState
    ): NewContactEvents()
    data class FocusChangeNameTxtField(
        val focusState: FocusState
    ): NewContactEvents()
    data class FocusChangeTimeZoneTxtField(
        val focusState: FocusState
    ): NewContactEvents()
    data class OnToggleSelection(
        val selectedOption : TimeZoneInfo
    ): NewContactEvents()
    data class OnSearchTxt(
        val text : String
    ): NewContactEvents()
    object OnDone : NewContactEvents()
    object OnSaveAndContinue : NewContactEvents()

    data class EditContact(
        val contactEntity: ContactEntity
    ) : NewContactEvents()

    data object DismissDialog : NewContactEvents()
    data object ShowDialog : NewContactEvents()
}

