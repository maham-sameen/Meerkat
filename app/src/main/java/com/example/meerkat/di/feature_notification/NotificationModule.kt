package com.example.meerkat.di.feature_notification

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.core.calendar.domain.repository.CalendarEntityRepo
import com.example.meerkat.core.calendar.domain.repository.EventRepo
import com.example.meerkat.feature_notification.data.data_source.mapper.EventDataToNotificationEventMapper
import com.example.meerkat.feature_notification.data.repository.NotificationRepoImp
import com.example.meerkat.feature_notification.domain.repository.NotificationRepo
import com.example.meerkat.feature_notification.domain.use_case.GetNotificationEvents
import com.example.meerkat.feature_notification.domain.use_case.GetTimeZoneUseCase
import com.example.meerkat.feature_notification.domain.use_case.NotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object NotificationModule {


    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @ViewModelScoped
    fun provideNotificationRepo(eventRepo: EventRepo , calendarEntityRepo: CalendarEntityRepo): NotificationRepo {
        return NotificationRepoImp(
            eventRepo = eventRepo,
            eventDataToNotificationEventMapper = EventDataToNotificationEventMapper(),
            calendarEntityRepo = calendarEntityRepo
        )
    }


    @Provides
    @ViewModelScoped
    fun provideNotificationUseCase(notificationRepo: NotificationRepo): NotificationUseCase {
        return NotificationUseCase(
            getNotificationEvents = GetNotificationEvents(notificationRepo) ,
            getTimeZoneUseCase = GetTimeZoneUseCase(notificationRepo)
        )
    }


}