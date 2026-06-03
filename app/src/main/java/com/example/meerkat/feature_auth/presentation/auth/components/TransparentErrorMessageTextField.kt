package com.example.meerkat.feature_auth.presentation.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.meerkat.core.presentation.ErrorMessageTextFieldState
import com.example.meerkat.ui.theme.blue400
import com.example.meerkat.ui.theme.blue800

@Composable
fun TransparentErrorMessageTextField(
    errorMessageTextFieldState: ErrorMessageTextFieldState,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    isValidEmail : (String) -> Unit,
    hint : String
) {


    Column(modifier = modifier) {
        OutlinedTextField(
            value = errorMessageTextFieldState.text,
            onValueChange = {
                onValueChange(it)
            },
            singleLine = true,
            textStyle = TextStyle.Default,
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor =MaterialTheme.colorScheme.surfaceContainerHighest ,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest ,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = blue400,
                unfocusedLabelColor = blue800,
                focusedBorderColor = if (errorMessageTextFieldState.isError) Color.Red else blue400,
                unfocusedBorderColor = if (errorMessageTextFieldState.isError) Color.Red else blue400 ,
                cursorColor = blue400
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, // Set to email type
                imeAction = ImeAction.Done // Show "Done" or "Go"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                   isValidEmail(errorMessageTextFieldState.text)
                }
            ) , 
            label = {
                Text(
                     text = hint ,
                    fontSize = 15.sp
                )
            }
        )

        // Show error message if validation fails
        if (errorMessageTextFieldState.isError) {
            Text(
                text = "Invalid email address",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


