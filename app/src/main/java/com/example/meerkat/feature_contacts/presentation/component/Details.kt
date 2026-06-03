package com.example.meerkat.feature_contacts.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.unit.dp
import com.example.meerkat.R


@Composable
fun TimeZoneComp(
    timeZoneName : String ,
    isFocused : Boolean ,
    isError : Boolean ,
    onFocusedChanged : (FocusState) -> Unit ,
    onSelectTimeZone : () -> Unit ,
    onTextChanged : (String) -> Unit ,
) {
    DetailComp(
        iconModifier = Modifier.size(30.dp),
        painter = R.drawable.baseline_public_24,
        field = {
            ContactTxtField(
                text = timeZoneName,
                hint = "Enter Time Zone",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "",
                        modifier = Modifier
                            .size(25.dp)
                            .clickable(
                                onClick = onSelectTimeZone
                            )
                        ,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                enabled = false ,
                isFocused = isFocused ,
                onFocusChanged = onFocusedChanged,
                isError = isError ,
                onTextChanged = onTextChanged
            )
        }
    )
}


@Composable
fun EnterEmailComp(
    emailTxtField : @Composable () -> Unit
) {
    DetailComp(
        imageVector = Icons.Outlined.Email,
        field = {
            emailTxtField()
        }
    )
}


@Composable
fun PhoneNumberComp(
    phoneNoTxtField : @Composable () -> Unit
) {
    DetailComp(
        painter = R.drawable.baseline_phone_android_24,
        field = {
            phoneNoTxtField()
        }
    )
}