package com.example.meerkat.feature_auth.domain.use_case.auth


import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.AuthRepo

class SignUpEmail(
    private val authRepo: AuthRepo,
    private val makeUniqueAccount: MakeUniqueAccount
) {
     suspend operator fun invoke(
        email : String ,
        password : String ,
        name : String ,
        users : List<User>?
    ) : AuthResult{
         if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
             return AuthResult(
                 data = null,
                 errorMessage = "Please fill email , password and name"
             )
         }
         var authResult =  authRepo.signUpWithEmail(
             email = email,
             password = password
         )
        if(authResult.data != null){
            val data  = authResult.data
            authResult = authResult.copy(
                data = data.copy(
                    name = name
                )
            )
        }
         return makeUniqueAccount.invoke(
             users = users,
             authResult = authResult,
         )
    }
}