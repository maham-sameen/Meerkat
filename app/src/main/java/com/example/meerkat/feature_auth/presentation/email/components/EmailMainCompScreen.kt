package com.example.meerkat.feature_auth.presentation.email.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.meerkat.core.presentation.component.FilledButtonWithBlueBack
import com.example.meerkat.feature_auth.presentation.email.EmailViewModel
import com.example.meerkat.feature_auth.presentation.email.EmailUIEvent


@Composable
fun EmailMainCompScreen(
    emailViewModel: EmailViewModel
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Surface (
        tonalElevation = 5.dp ,
        shadowElevation = 5.dp ,
        color = MaterialTheme.colorScheme.surface ,
        shape = RoundedCornerShape(5.dp)
    ){
        Column (
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            modifier = Modifier.padding(
                horizontal = 20.dp ,
                vertical = 40.dp)
        ){

            val textModifier = Modifier.align(Alignment.Start)
            Spacer(Modifier.height(40.dp))
            Text(
                modifier = textModifier ,
                text = "Enter your email to get started"
            )
            Spacer(Modifier.height(10.dp))
            Box{
                SimpleTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                    ,
                    text = emailViewModel.emailText.value,
                    onValueChange = { emailViewModel.onEvent(EmailUIEvent.EnterEmail(it)) },
                    hint = "Enter email",
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email, // Set to email type
                        imeAction = ImeAction.Next// Show "Done" or "Go"
                    ),
                    singleLine = true
                )
            }
            Spacer(Modifier.height(40.dp))
            Text(
                modifier = textModifier,
                text = "Enter your full name"
            )
            Spacer(Modifier.height(10.dp))
            Box{
                SimpleTextField(
                    text = emailViewModel.nameText.value,
                    onValueChange = { emailViewModel.onEvent(EmailUIEvent.EnterName(it)) },
                    hint = "Enter Name",
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, // Set to email type
                        imeAction = ImeAction.Next // Show "Done" or "Go"
                    ) ,
                    singleLine = true
                )
            }
            Spacer(Modifier.height(40.dp))
            Text(
                modifier = textModifier,
                text = "Choose a password of at least \n 8 characters."
            )
            Spacer(Modifier.height(10.dp))
            Box{
                SimpleTextField(
                    text = emailViewModel.visibilityPasswordState.value.password,
                    onValueChange = { emailViewModel.onEvent(EmailUIEvent.EnterPassword(it)) },
                    hint = "password",
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, // Set to email type
                        imeAction = ImeAction.Done // Show "Done" or "Go"
                    ),
                    isPassword = true,
                    onPasswordToggle = { emailViewModel.onEvent(EmailUIEvent.OnToggleVisibility) },
                    visualTransformation = emailViewModel.visibilityPasswordState.value.visualTransformation,
                    visibility = painterResource(emailViewModel.visibilityPasswordState.value.painter),
                    onDone = {
                          // ✅ clears text field focus
                        emailViewModel.onEvent(EmailUIEvent.AuthWithEmail)
                    } ,
                    singleLine = true
                )
            }
            PasswordStrengthCalculator(
                modifier = textModifier,
                passwordStrengthLevels = emailViewModel.passwordStrengthLevels.value
            )
            Spacer(Modifier.height(20.dp))
            FilledButtonWithBlueBack(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                text = "Continue",
                elevation = 10.dp ,
                onClick = {emailViewModel.onEvent(EmailUIEvent.AuthWithEmail)}
            )
        }
    }
}

