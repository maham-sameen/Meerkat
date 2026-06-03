package com.example.meerkat.feature_contacts.presentation

import com.example.meerkat.core.presentation.FocusedErrorTxtFieldState
import com.togitech.ccp.data.PhoneCode

data class PhoneNoState(
    val code : PhoneCode = "+92" ,
    val phoneNoTxtFieldState  : FocusedErrorTxtFieldState = FocusedErrorTxtFieldState()
)

