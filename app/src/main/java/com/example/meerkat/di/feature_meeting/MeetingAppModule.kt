package com.example.meerkat.di.feature_meeting

import android.content.Context
import androidx.work.WorkManager
import com.example.meerkat.core.calendar.data.data_source.local.AppDatabase
import com.example.meerkat.core.calendar.data.data_source.local.AuthLocalDataSource
import com.example.meerkat.core.calendar.domain.repository.CalendarEntityRepo
import com.example.meerkat.core.calendar.domain.repository.EventRepo
import com.example.meerkat.feature_contacts.domain.repository.ContactRepo
import com.example.meerkat.feature_meetings.data.data_source.local.EventContactCrossRefDao
import com.example.meerkat.feature_meetings.data.data_source.local.MeetingLocalDataSource
import com.example.meerkat.feature_meetings.data.data_source.remote.MeetingApi
import com.example.meerkat.feature_meetings.data.data_source.remote.EmailRemoteDataSource
import com.example.meerkat.feature_meetings.data.repository.MeetingRepoImp
import com.example.meerkat.feature_meetings.data.repository.CalendarAvailabilityRepoImp
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MeetingAppModule {



    @Provides
    @Singleton
    fun provideEventContactCrossRefDao(appDatabase: AppDatabase): EventContactCrossRefDao {
        return appDatabase.eventContactCrossRefDao()
    }


    @Provides
    @Singleton
    fun provideMeetingLocalDataSource(@ApplicationContext context : Context): MeetingLocalDataSource{
        return MeetingLocalDataSource(context)
    }


    @Singleton
    @Provides
    fun provideMeetingApi(retrofit: Retrofit): MeetingApi {
       return retrofit.create(MeetingApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMeetingRepoImp(dataSource: AuthLocalDataSource,
                              api : MeetingApi,
                              calendarEntityRepo: CalendarEntityRepo ,
                              meetingLocalDataSource: MeetingLocalDataSource
    )
    : CalendarAvailabilityRepo {
        return CalendarAvailabilityRepoImp(
            dataSource = dataSource,
            api = api,
            calendarEntityRepo = calendarEntityRepo,
            meetingLocalDataSource = meetingLocalDataSource,
        )
    }

    @Singleton
    @Provides
    fun provideConfirmMeetingRepo(
        contactRepo: ContactRepo ,
        eventContactCrossRefDao: EventContactCrossRefDao ,
        eventRepo: EventRepo
    ): MeetingRepo{
        return MeetingRepoImp(
            contactRepo = contactRepo,
            eventContactCrossRefDao = eventContactCrossRefDao,
            eventRepo = eventRepo,
            emailRemoteDataSource = EmailRemoteDataSource()
        )
    }

}