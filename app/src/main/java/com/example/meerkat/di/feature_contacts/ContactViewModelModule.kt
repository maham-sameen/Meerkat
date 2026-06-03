package com.example.meerkat.di.feature_contacts

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.core.time_zone.domain.use_case.GetTimeZonesUseCase
import com.example.meerkat.feature_contacts.domain.repository.ContactRepo
import com.example.meerkat.feature_contacts.domain.use_case.ContactUseCases
import com.example.meerkat.feature_contacts.domain.use_case.GetAllContacts
import com.example.meerkat.feature_contacts.domain.use_case.GetCurrentUserId
import com.example.meerkat.feature_contacts.domain.use_case.InsertNewContact
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object ContactViewModelModule {

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @ViewModelScoped
    fun provideNewContactUseCase(
        contactRepo: ContactRepo ,
        getTimeZonesUseCase: GetTimeZonesUseCase
    ): ContactUseCases {
        return ContactUseCases(
            insertNewContact = InsertNewContact(contactRepo),
            getTimeZonesUseCase = getTimeZonesUseCase,
            getAllContacts = GetAllContacts(contactRepo),
            getCurrentUserId = GetCurrentUserId(contactRepo)
        )
    }
}