package com.example.meerkat.keycloak.auth.di

import android.content.Context
import com.example.meerkat.keycloak.auth.util.AuthLocalDataSourceKeycloak
import com.example.meerkat.keycloak.auth.util.AuthRemoteClientUtilsKeycloak
import com.example.meerkat.keycloak.auth.util.PkceHelperKeycloak
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {


    @Singleton
    @Provides
    fun provideAuthRemoteClientUtils(@ApplicationContext context : Context) : AuthRemoteClientUtilsKeycloak{
        return AuthRemoteClientUtilsKeycloak(
            authLocalDataSource = AuthLocalDataSourceKeycloak(context),
            pkceHelperKeycloak = PkceHelperKeycloak()
        )
    }
}