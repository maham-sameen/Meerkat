package com.example.meerkat.di.feature_contacts

import com.example.meerkat.core.calendar.data.data_source.local.AppDatabase
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import com.example.meerkat.feature_contacts.data.data_source.local.ContactDao
import com.example.meerkat.feature_contacts.data.repository.ContactRepoImp
import com.example.meerkat.feature_contacts.domain.repository.ContactRepo
import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ContactAppModule {


    @Provides
    @Singleton
    fun provideContactDao(
        appDatabase: AppDatabase
    ): ContactDao {
        return appDatabase.contactDao()
    }

    @Provides
    @Singleton
    fun provideContactRepo(contactDao: ContactDao, userRepo: UserRepo, calendarAvailabilityRepo: CalendarAvailabilityRepo) : ContactRepo {
        return ContactRepoImp(
            contactDao = contactDao,
            userRepo = userRepo,
            calendarAvailabilityRepo = calendarAvailabilityRepo
        )
    }
}