package com.example.meerkat.di.feature_meeting

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.core.calendar.domain.repository.CalendarRepo
import com.example.meerkat.core.calendar.domain.use_case.GetMeerkatEventLinks
import com.example.meerkat.core.time_zone.domain.use_case.GetTimeZonesUseCase
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.example.meerkat.feature_meetings.domain.repository.CalendarAvailabilityRepo
import com.example.meerkat.feature_meetings.domain.use_case.ExtractNameFromEmail
import com.example.meerkat.feature_meetings.domain.use_case.GetMeetingDescription
import com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting.ConfirmMeetingUseCase
import com.example.meerkat.feature_meetings.domain.use_case.FormateTimeSlotDetailed
import com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting.GetAllContacts
import com.example.meerkat.feature_meetings.domain.use_case.meeting_days.GetBusyTimeList
import com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting.GetCurrentUser
import com.example.meerkat.feature_meetings.domain.use_case.meeting_snap_shots.GetMeetingSnapShots
import com.example.meerkat.feature_meetings.domain.use_case.MapContactToAttendee
import com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting.MapUserToMeetingAttendee
import com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting.OnScheduleMeeting
import com.example.meerkat.feature_meetings.domain.use_case.GetHost
import com.example.meerkat.feature_meetings.domain.use_case.meeting_days.GetMeetingDates
import com.example.meerkat.feature_meetings.domain.use_case.GetTZCalender
import com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting.GetToUpdateConfirmMeeting
import com.example.meerkat.feature_meetings.domain.use_case.confirm_meeting.SaveEvent
import com.example.meerkat.feature_meetings.domain.use_case.meeting_days.GetToUpdateEventTimeSlot
import com.example.meerkat.feature_meetings.domain.use_case.meeting_days.MeetingsUseCases
import com.example.meerkat.feature_meetings.domain.use_case.meeting_snap_shots.DeleteMeetingSnapshot
import com.example.meerkat.feature_meetings.domain.use_case.scheduled_meetings_details.GetMeetingDetails
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Provider


@Module
@InstallIn(ViewModelComponent:: class)
object MeetingViewModelModule {




    @RequiresApi(Build.VERSION_CODES.O)
    @ViewModelScoped
    @Provides
    fun provideMeetingsUseCase(calendarAvailabilityRepo: CalendarAvailabilityRepo ,
                               getHost: GetHost ,
                               getTimeZonesUseCase: GetTimeZonesUseCase ,
                               getTZCalender: GetTZCalender ,
                               meetingRepo: MeetingRepo ,
    ): MeetingsUseCases {
        return MeetingsUseCases(
            getBusyTimeList = GetBusyTimeList(
                calendarAvailabilityRepo),
            getMeetingDates = GetMeetingDates(),
            getTZCalender = getTZCalender,
            getTimeZonesUseCase = getTimeZonesUseCase,
            getHost = getHost,
            toUpdateMeetings = GetToUpdateEventTimeSlot(meetingRepo),
        )
    }

    @ViewModelScoped
    @Provides
    fun providesGetTZCalendar(
        calendarAvailabilityRepo: CalendarAvailabilityRepo
    ): GetTZCalender{
        return GetTZCalender(calendarAvailabilityRepo)
    }

    @ViewModelScoped
    @Provides
    fun provideExtractNameFromEmail(): ExtractNameFromEmail{
        return ExtractNameFromEmail()
    }


    @ViewModelScoped
    @Provides
    fun provideGetHost(
        calendarAvailabilityRepo: CalendarAvailabilityRepo ,
        extractNameFromEmail: ExtractNameFromEmail
    ): GetHost{
        return GetHost(
            calendarAvailabilityRepo,
            extractNameFromEmail = extractNameFromEmail
        )
    }


    @ViewModelScoped
    @Provides
    fun provideFormateTimeSlotDetailed(): FormateTimeSlotDetailed{
        return FormateTimeSlotDetailed()
    }

    @ViewModelScoped
    @Provides
    fun provideMapContactToAttendee(): MapContactToAttendee{
        return MapContactToAttendee()
    }

    @ViewModelScoped
    @Provides
    fun provideConfirmMeetingUseCase(
        meetingRepo: MeetingRepo,
        getTZCalender: GetTZCalender,
        formateTimeSlotDetailed: FormateTimeSlotDetailed,
        mapContactToAttendee: MapContactToAttendee ,
        getMeerkatJoinLink: GetMeerkatEventLinks ,
        extractNameFromEmail: ExtractNameFromEmail ,
        calendarAvailabilityRepo: CalendarAvailabilityRepo ,
        calendarRepo: Provider<CalendarRepo>
    ): ConfirmMeetingUseCase{
        return ConfirmMeetingUseCase(
            getAllContacts = GetAllContacts(meetingRepo),
            getCurrentUser = GetCurrentUser(meetingRepo),
            formateTimeSlotDetailed = formateTimeSlotDetailed,
            mapUserToMeetingAttendee = MapUserToMeetingAttendee(),
            mapContactToAttendee = mapContactToAttendee,
            onScheduleMeeting = OnScheduleMeeting(
                getTZCalender = getTZCalender,
                meetingRepo = meetingRepo,
                geMeerkatJoinLink = getMeerkatJoinLink,
                extractNameFromEmail = extractNameFromEmail,
                calendarAvailabilityRepo = calendarAvailabilityRepo,
                saveEvent = SaveEvent(calendarRepo)
            ),
            getToUpdateConfirmMeeting = GetToUpdateConfirmMeeting(
                meetingRepo = meetingRepo,
                mapContactToAttendee = mapContactToAttendee
            ),
        )
    }


    @Provides
    @ViewModelScoped
    fun provideMeetingDetailsUseCase(
        meetingRepo: MeetingRepo,
        formateTimeSlotDetailed: FormateTimeSlotDetailed,
        meetingDescription: GetMeetingDescription,
        getMeerkatEventLinks: GetMeerkatEventLinks,
        mapContactToAttendee: MapContactToAttendee
    ): GetMeetingDetails{
        return GetMeetingDetails(
            meetingRepo = meetingRepo,
            formateTimeSlotDetailed = formateTimeSlotDetailed,
            meetingDescription = meetingDescription,
            getMeerkatEventLinks = getMeerkatEventLinks,
            mapContactToAttendee = mapContactToAttendee
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetMeetingDescription(): GetMeetingDescription{
        return GetMeetingDescription()
    }


    @ViewModelScoped
    @Provides
    fun provideGetMeetingSnapShots(confirmationRepo: MeetingRepo,
                                   getMeerkatEventLinks: GetMeerkatEventLinks,
                                   formateTimeSlotDetailed: FormateTimeSlotDetailed,
                                   mapContactToAttendee: MapContactToAttendee,
                                   getMeetingDescription: GetMeetingDescription
    ): GetMeetingSnapShots{
        return GetMeetingSnapShots(
            meetingRepo = confirmationRepo,
            formateTimeSlotDetailed = formateTimeSlotDetailed,
            mapContactToAttendee = mapContactToAttendee,
            getMeerkatEventLinks = getMeerkatEventLinks,
            getMeetingDescription = getMeetingDescription
        )
    }



    @ViewModelScoped
    @Provides
    fun provideDetailMeetingSnapshots(
        meetingRepo: MeetingRepo ,
        calendarAvailabilityRepo: CalendarAvailabilityRepo
    ): DeleteMeetingSnapshot{
        return DeleteMeetingSnapshot(
            meetingRepo = meetingRepo,
            calendarAvailabilityRepo = calendarAvailabilityRepo
        )
    }
}