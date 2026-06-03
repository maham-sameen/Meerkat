package com.example.meerkat.di.core.calendar

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.meerkat.core.calendar.data.data_source.local.AppDatabase
import com.example.meerkat.core.calendar.data.data_source.local.AuthLocalDataSource
import com.example.meerkat.core.calendar.data.data_source.local.CalendarDao
import com.example.meerkat.core.calendar.data.data_source.local.EventDao
import com.example.meerkat.core.calendar.data.data_source.mapper.CalendarInfoDtoToCalendarEntity
import com.example.meerkat.core.calendar.data.data_source.remote.CalendarApi
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import com.example.meerkat.core.calendar.data.repository.CalendarEntityRepoImp
import com.example.meerkat.core.calendar.data.repository.CalendarRepoImp
import com.example.meerkat.core.calendar.data.repository.EventRepoImp
import com.example.meerkat.core.calendar.domain.repository.CalendarEntityRepo
import com.example.meerkat.core.calendar.domain.repository.CalendarRepo
import com.example.meerkat.core.calendar.domain.repository.EventRepo
import com.example.meerkat.core.util.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object CalendarAppModule {
    @Provides
    @Singleton
    fun  provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            ConstantCalendar.DATABASE_NAME
        ).build()
    }




    @Provides
    @Singleton
    fun provideCalendarDao(appDatabase: AppDatabase): CalendarDao {
        return  appDatabase.calendarDao()
    }

    @Provides
    @Singleton
    fun provideEventDao(appDatabase: AppDatabase): EventDao {
        return appDatabase.eventDao()
    }


    @Provides
    @Singleton
    fun provideCalendarRepository(db : AppDatabase): CalendarRepo {
        return CalendarRepoImp(
            attachmentDao = db.attachmentDao(),
            attendeeDao = db.attendeeDao(),
            conferenceDataDao = db.conferenceDataDao(),
            entryPointDao = db.entryPointDao(),
            eventDao = db.eventDao(),
            calendarDao = db.calendarDao(),
            overrideDao = db.overrideDao(),
            reminderDao = db.reminderDao(),
        )
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val BASE_URL = "https://www.googleapis.com/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideGoogleCalendar(retrofit: Retrofit): CalendarApi {
        return  retrofit.create(CalendarApi::class.java)
    }





    @Provides
    @Singleton
    fun provideAuthLocalDataSource(@ApplicationContext context: Context): AuthLocalDataSource {
        return AuthLocalDataSource(context)
    }



    @Singleton
    @Provides
    fun provideEventRepo(
        eventDao: EventDao,
    ) : EventRepo {
        return EventRepoImp(
            eventDao = eventDao
        )
    }



    @Singleton
    @Provides
    fun provideCalendarEntityRepo(calendarDao: CalendarDao , dataSource: AuthLocalDataSource , api : CalendarApi , ): CalendarEntityRepo {
        return CalendarEntityRepoImp(
            calendarDao = calendarDao,
            dataSource = dataSource,
            api = api,
            calendarInfoDtoToCalendarEntity = CalendarInfoDtoToCalendarEntity()
        )
    }


    @Singleton
    @Provides
    fun provideSessionManager(
        authLocalDataSource: AuthLocalDataSource
    ): SessionManager {
        return SessionManager(
            authLocalDataSource = authLocalDataSource
        )
    }

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)

}