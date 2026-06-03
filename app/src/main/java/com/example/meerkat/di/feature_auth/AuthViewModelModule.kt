package com.example.meerkat.di.feature_auth

import com.example.meerkat.feature_auth.domain.repository.AuthRepo
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import com.example.meerkat.feature_auth.domain.use_case.AuthUseCases
import com.example.meerkat.feature_auth.domain.use_case.CheckPasswordStrength
import com.example.meerkat.feature_auth.domain.use_case.GetUser
import com.example.meerkat.feature_auth.domain.use_case.GetUsers
import com.example.meerkat.feature_auth.domain.use_case.auth.AuthGoogleClient
import com.example.meerkat.feature_auth.domain.use_case.auth.AuthMicrosoftClient
import com.example.meerkat.feature_auth.domain.use_case.auth.HandleAuthorization
import com.example.meerkat.feature_auth.domain.use_case.auth.IsValidEmailAddress
import com.example.meerkat.feature_auth.domain.use_case.auth.MakeUniqueAccount
import com.example.meerkat.feature_auth.domain.use_case.auth.SignInEmail
import com.example.meerkat.feature_auth.domain.use_case.auth.SignUpEmail
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object AuthViewModelModule {


    @ViewModelScoped
    @Provides
    fun provideUniqueAccountUseCase(userRepo: UserRepo) : MakeUniqueAccount {
        return MakeUniqueAccount(
            repository = userRepo
        )
    }

    @ViewModelScoped
    @Provides
    fun provideAuthUseCases(authRepo: AuthRepo, userRepo: UserRepo, uniqueAccountUseCase: MakeUniqueAccount): AuthUseCases {
        return AuthUseCases(
            authGoogleClient = AuthGoogleClient(
                authRepo = authRepo
            ),
            handleAuthorization = HandleAuthorization(
                authRepo = authRepo,
                makeUniqueAccount = uniqueAccountUseCase
            ),
            authMicrosoftClient = AuthMicrosoftClient(
                makeUniqueAccount = uniqueAccountUseCase
            ),
            signInEmail = SignInEmail(
                authRepo = authRepo
            ),
            checkPasswordStrength = CheckPasswordStrength(),
            signUpEmail = SignUpEmail(
                authRepo = authRepo,
                makeUniqueAccount = uniqueAccountUseCase
            ),
            getUsers = GetUsers(
                repository = userRepo
            ),
            getUser = GetUser(
                repository = userRepo
            ),
            isValidEmailAddress = IsValidEmailAddress()
        )
    }
}