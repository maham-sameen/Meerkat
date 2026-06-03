package com.example.meerkat.feature_auth.data.data_source.mapper

import com.example.meerkat.feature_auth.domain.model.AuthResult


fun setFailedAuthResult(message: String): AuthResult {
    return AuthResult(
        data = null,
        errorMessage = message
    )
}