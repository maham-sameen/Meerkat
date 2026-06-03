package com.example.meerkat.di.feature_auth

import com.example.meerkat.feature_auth.data.data_source.remote.EmailClientUtils
import com.example.meerkat.feature_auth.data.data_source.remote.UserRemoteDataSource
import com.example.meerkat.feature_auth.data.repository.FakeAuthRepo
import com.example.meerkat.feature_auth.data.repository.FakeUserRepo
import com.example.meerkat.feature_auth.domain.repository.AuthRepo
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthAppModule::class] // ⬅️ this replaces the real one
)
object TestAuthAppModule {

    @Singleton
    @Provides
    fun provideAuthRepo(
        emailClientUtils: EmailClientUtils
    ): AuthRepo{
        return FakeAuthRepo(
            emailClientUtils = emailClientUtils
        )
    }

    @Singleton
    @Provides
    fun provideUserRepo(
        userRemoteDataSource: UserRemoteDataSource
    ): UserRepo{
        return FakeUserRepo(
            userRemoteDataSource = userRemoteDataSource
        )
    }
}