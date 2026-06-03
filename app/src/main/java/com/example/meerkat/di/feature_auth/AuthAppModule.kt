package com.example.meerkat.di.feature_auth

import com.example.meerkat.core.calendar.data.data_source.local.AuthLocalDataSource
import com.example.meerkat.core.calendar.data.data_source.model.remote.PkceHelper
import com.example.meerkat.feature_auth.data.data_source.remote.AuthRemoteClientUtils
import com.example.meerkat.feature_auth.data.data_source.remote.EmailClientUtils
import com.example.meerkat.feature_auth.data.data_source.remote.UserRemoteDataSource
import com.example.meerkat.feature_auth.data.repository.AuthRepoImp
import com.example.meerkat.feature_auth.data.repository.UserRepoImp
import com.example.meerkat.feature_auth.domain.repository.AuthRepo
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AuthAppModule{

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        emailClientUtils: EmailClientUtils,
        pkceHelper: PkceHelper,
        authLocalDataSource: AuthLocalDataSource,
    ): AuthRemoteClientUtils {
        return AuthRemoteClientUtils(
            emailClientUtils = emailClientUtils,
            authLocalDataSource = authLocalDataSource,
            pkceHelper = pkceHelper,
        )
    }


    @Provides
    @Singleton
    fun provideAuthRepository(remoteDataSource: AuthRemoteClientUtils): AuthRepo {
        return AuthRepoImp(remoteDataSource)
    }



    @Singleton
    @Provides
    fun providesUserRepository(remoteDataSource: UserRemoteDataSource): UserRepo {
        return UserRepoImp(remoteDataSource = remoteDataSource)
    }
}

//https://stackoverflow.com/questions/70633726/hilt-circular-dependency?noredirect=1&lq=1

