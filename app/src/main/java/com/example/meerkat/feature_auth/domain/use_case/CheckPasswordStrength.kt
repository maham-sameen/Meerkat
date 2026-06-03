package com.example.meerkat.feature_auth.domain.use_case

import com.example.meerkat.feature_auth.domain.util.PasswordStrengthLevels

class CheckPasswordStrength {

    private val specialCharacters = "!@#$%^&*()_=+{}/.<>|\\[\\]~-".toSet()  // Use a Set for O(1) lookup

    operator fun invoke(password: String?): PasswordStrengthLevels {
        if (password.isNullOrEmpty()) return PasswordStrengthLevels.LOW


        var score = 0
        var hasLower = false
        var hasUpper = false
        var hasDigit = false
        var hasSpecial = false

        for (char in password) {
            when {
                char.isLowerCase() -> hasLower = true
                char.isUpperCase() -> hasUpper = true
                char.isDigit() -> hasDigit = true
                specialCharacters.contains(char) -> hasSpecial = true
            }
        }

        score += if (hasLower) 1 else 0
        score += if (hasUpper) 1 else 0
        score += if (hasDigit) 1 else 0
        score += if (hasSpecial) 1 else 0


        return when {
            password.length >= 16 && score >= 4 -> PasswordStrengthLevels.EXCELLENT
            password.length >= 12 && score >= 3 -> PasswordStrengthLevels.GOOD
            password.length >= 8 && score >= 2 -> PasswordStrengthLevels.FAIR
            else -> PasswordStrengthLevels.LOW
        }
    }
}
