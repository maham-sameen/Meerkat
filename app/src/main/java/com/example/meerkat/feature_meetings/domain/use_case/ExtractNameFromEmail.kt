package com.example.meerkat.feature_meetings.domain.use_case

class ExtractNameFromEmail {
    operator fun invoke(email : String): String{
        val username = email.substringBefore("@")
        val name = username
            .replace(Regex("[^a-zA-Z]"), " ") // Remove digits and symbols
            .split(" ")                       // Split on spaces
            .filter { it.isNotBlank() }       // Remove empty parts
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } } // Capitalize
        return name.trim()
    }
}