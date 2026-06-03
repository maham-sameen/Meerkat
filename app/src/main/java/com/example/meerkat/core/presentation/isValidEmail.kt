package com.example.meerkat.core.presentation

import android.util.Patterns
import androidx.core.util.PatternsCompat


// Function to validate email
fun CharSequence.isValidEmail(): Boolean {
   return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}