package com.example.meerkat.feature_auth.presentation.email.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.feature_auth.presentation.component.getTextFieldColors

@Preview(showBackground = true)
@Preview("dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_C, showBackground = true)
@Composable
fun SimpleTextFieldPreview() {
}

@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    hint: String,
    keyBoardOptions: KeyboardOptions,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    isPassword: Boolean = false, // Add a parameter for password visibility
    onPasswordToggle: () -> Unit  = {},// This is to handle password visibility toggle  ,
    visualTransformation : VisualTransformation = VisualTransformation.None,
    visibility: Painter = painterResource(R.drawable.baseline_visibility_off_24) ,
    onDone : () -> Unit = {} ,
) {
   // val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = singleLine,
        textStyle = textStyle,
        modifier = modifier.fillMaxWidth(),
        colors = getTextFieldColors(),
        keyboardOptions = keyBoardOptions,
        label = {
            Text(
                text = hint,
                fontSize = 15.sp
            )
        },
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { onPasswordToggle() }) {
                    Icon(
                        painter = visibility ,
                        contentDescription = "decide to show or hide the password"
                    )
                }
            }
        } ,
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onDone()
            } ,
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
    )
}

//https://stackoverflow.com/questions/76228381/how-to-hide-the-keyboard-in-dialog-with-compose
