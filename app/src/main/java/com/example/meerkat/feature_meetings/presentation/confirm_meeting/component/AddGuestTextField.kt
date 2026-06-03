package com.example.meerkat.feature_meetings.presentation.confirm_meeting.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.meerkat.ui.theme.blue400


@Composable
fun AddGuestTextField(
    modifier: Modifier,
    text : String = "",
    onTextChanged : (String) -> Unit = {},
    onDone: () -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChanged,
        modifier = Modifier
            .heightIn(min = 60.dp)
            .fillMaxWidth()
            .then(modifier)
        ,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer ,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer ,
            focusedIndicatorColor = blue400 ,
            cursorColor = blue400
        ) ,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ) ,
    )
}
