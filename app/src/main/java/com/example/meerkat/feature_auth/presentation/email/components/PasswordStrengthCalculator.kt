package com.example.meerkat.feature_auth.presentation.email.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.meerkat.feature_auth.domain.util.PasswordStrengthLevels


@Composable
fun PasswordStrengthCalculator(modifier: Modifier , passwordStrengthLevels: PasswordStrengthLevels) {

    val transition = updateTransition(targetState = passwordStrengthLevels, label = "PasswordStrengthTransition")

    val progressLevel by transition.animateFloat(label = "ProgressLevel") { strength ->
        when (strength) {
            PasswordStrengthLevels.LOW ->  0.2f
            PasswordStrengthLevels.FAIR -> 0.5f
            PasswordStrengthLevels.GOOD -> 0.7f
            PasswordStrengthLevels.EXCELLENT -> 1f
        }
    }

    val strengthColor by transition.animateColor(label = "StrengthColor") { strength ->
        when (strength) {
            PasswordStrengthLevels.LOW -> PasswordStrengthLevels.LOW.strength.strengthColor
            PasswordStrengthLevels.FAIR -> PasswordStrengthLevels.FAIR.strength.strengthColor
            PasswordStrengthLevels.GOOD -> PasswordStrengthLevels.GOOD.strength.strengthColor
            PasswordStrengthLevels.EXCELLENT -> PasswordStrengthLevels.EXCELLENT.strength.strengthColor
        }
    }

    Spacer(Modifier.height(10.dp))
    AnimatedVisibility( passwordStrengthLevels != PasswordStrengthLevels.LOW ,
        modifier =modifier) {
        Text(
            text = stringResource(passwordStrengthLevels.strength.strengthLevel),
            color = passwordStrengthLevels.strength.strengthColor
        )
    }
    Spacer(modifier
        .height(10.dp)
        .fillMaxWidth(progressLevel)
        .background(strengthColor))
    Spacer(Modifier.height(10.dp))
}