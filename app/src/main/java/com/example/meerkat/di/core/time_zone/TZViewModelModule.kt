package com.example.meerkat.di.core.time_zone

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.core.time_zone.domain.use_case.GetTimeZonesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object TZViewModelModule {


    @RequiresApi(Build.VERSION_CODES.O)
    @ViewModelScoped
    @Provides
    fun provideTimeZoneUseCase(): GetTimeZonesUseCase{
        return GetTimeZonesUseCase()
    }
}