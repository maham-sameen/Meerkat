package com.example.meerkat.feature_contacts.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.ui.theme.blue400

@Composable
fun ContactTxtField(
    modifier: Modifier =Modifier
        .heightIn(min = 60.dp)
        .fillMaxWidth()
        .padding(end = 50.dp) ,
    fontSize : TextUnit =  15.sp  ,
    hint : String,
    containerColor : Color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.2f) ,
    trailingIcon : (@Composable () -> Unit)? = null,
    enabled : Boolean = true,
    text : String = "",
    onTextChanged : (String) -> Unit = {},
    isError : Boolean = false,
    isFocused : Boolean = false ,
    onFocusChanged : (FocusState) -> Unit = {},
    singleLine : Boolean = true ,

) {
    val focusManager  = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier
            .onFocusChanged { onFocusChanged(it) }
        ,
        value =text,
        enabled = enabled,
        onValueChange = onTextChanged,
        label = {
            if(!isFocused && text.isEmpty()){
                Text(text = hint , fontSize = fontSize)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = containerColor,
            focusedContainerColor =containerColor ,
            cursorColor = blue400 ,
            focusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            disabledContainerColor = containerColor ,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) ,
            focusedTextColor = MaterialTheme.colorScheme.onSurface ,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        ),
        textStyle = TextStyle.Default.copy(fontSize = fontSize),
        trailingIcon = trailingIcon,
        isError = isError ,
        singleLine = singleLine ,
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        ) ,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next

        )
    )
}

//https://stackoverflow.com/questions/76228381/how-to-hide-the-keyboard-in-dialog-with-compose