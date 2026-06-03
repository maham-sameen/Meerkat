package com.example.meerkat.feature_auth.domain.util

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.meerkat.R
import com.example.meerkat.ui.theme.excellentColor
import com.example.meerkat.ui.theme.fairColor
import com.example.meerkat.ui.theme.goodColor
import com.example.meerkat.ui.theme.lowColor

data class PasswordStrength(
    @StringRes
    val strengthLevel: Int = R.string.low,
    val strengthColor : Color = lowColor
)



enum class PasswordStrengthLevels(val strength: PasswordStrength) {
    LOW(PasswordStrength(R.string.low, lowColor)),
    FAIR(PasswordStrength(R.string.fair, fairColor)),
    GOOD(PasswordStrength(R.string.good, goodColor)),
    EXCELLENT(PasswordStrength(R.string.excellent, excellentColor))
}

