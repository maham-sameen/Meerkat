package com.example.meerkat.feature_auth.presentation.auth.components



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meerkat.R
import com.example.meerkat.feature_auth.presentation.auth.AuthLangState
import com.example.meerkat.core.presentation.component.FilledButtonWithBlueBack
import com.example.meerkat.ui.theme.blue400


@Composable
fun Authentication(
    authLangState: AuthLangState,
    onContinueEmailAddress: () -> Unit,
    getStartedWithGoogleAccount : () -> Unit,
    getStartedWithMicrosoftAccount : () -> Unit,
    onAction: () -> Unit,
    transparentErrorMessageTextFilled : @Composable () -> Unit
) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(55.dp)
    Surface (
        tonalElevation = 5.dp ,
        shadowElevation = 5.dp ,
        color = MaterialTheme.colorScheme.surface ,
        shape = RoundedCornerShape(5.dp)
    ){
        Column (
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            modifier = Modifier
                .padding(
                    horizontal = 20.dp ,
                    vertical = 40.dp
                )
        ){
            Spacer(Modifier.height(30.dp))
            Text(
                text = authLangState.enterEmail ,
                fontWeight = FontWeight.Bold ,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(20.dp))
            transparentErrorMessageTextFilled()
            Spacer(Modifier.height(20.dp))
            FilledButtonWithBlueBack(
                buttonModifier ,
                 authLangState.continueText,
                elevation = 1.dp,
                onClick = onContinueEmailAddress
            )
            Spacer(Modifier.height(20.dp))
            Spacer(Modifier.height(40.dp))
            Divider(authLangState.or)
            Spacer(Modifier.height(40.dp))
            AuthButton(
                icon = R.drawable.google_icon,
                text =authLangState.withGoogle ,
                modifier = buttonModifier,
                onClickLabel = stringResource(R.string.auth_with_google),
                onClick = getStartedWithGoogleAccount
            )
            Spacer(Modifier.height(10.dp))
            AuthButton(
                icon = R.drawable.microsoft,
                text = authLangState.withMicrosoft,
                modifier = buttonModifier,
                onClickLabel = stringResource(R.string.auth_with_micro),
                onClick = getStartedWithMicrosoftAccount
            )
            Spacer(Modifier.height(20.dp))
            HavingOrNotAccount(
                modifier = Modifier.align(Alignment.Start),
                havingAccount = authLangState.withOrNoAccount,
                actionText = authLangState.auth,
                onAction = onAction
            )
            Spacer(Modifier.height(40.dp))
        }
    }
}


@Composable
fun Divider(or : String = "OR") {
    Row (
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.Center
    ){
        HorizontalDivider(
            modifier =  Modifier.weight(1f) ,
            color = MaterialTheme.colorScheme.outlineVariant ,
            thickness = 2.dp
        )
        Text(
            text =  or ,
            modifier = Modifier.padding(horizontal = 5.dp) ,
            color = MaterialTheme.colorScheme.onSurface
        )
        HorizontalDivider(
            modifier =  Modifier.weight(1f) ,
            color = MaterialTheme.colorScheme.outlineVariant ,
            thickness = 2.dp
        )
    }
}



@Composable
fun HavingOrNotAccount(
    modifier: Modifier = Modifier,
    havingAccount : String,
    actionText : String,
    onAction : () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.Center ,
        modifier = modifier
    ) {
        Text(text = havingAccount)
        Text(
            text = actionText ,
            modifier = Modifier
                .padding(start = 5.dp)
                .clickable {
                    onAction()
                },
            color = blue400
            )
    }
}




