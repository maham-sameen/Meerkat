package com.example.meerkat.di.core.calendar

import com.example.meerkat.core.calendar.domain.use_case.GetMeerkatEventLinks
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
object CalendarViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideGetMeerkatLink(): GetMeerkatEventLinks{
        return GetMeerkatEventLinks()
    }
}