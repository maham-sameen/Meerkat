package com.example.meerkat.feature_auth.domain.use_case.auth

import com.example.meerkat.feature_auth.domain.repository.AuthRepo

class SignInEmail(
    private val authRepo: AuthRepo
) {
    operator suspend fun invoke(
        email : String ,
        password : String ,
    ) : Result<String>{
        if(email.isEmpty() || password.isEmpty()){
            return Result.success("Please fill email and password must")
        }
       return authRepo.signInWithEmail(
            email = email,
            password = password,
        )
    }
}