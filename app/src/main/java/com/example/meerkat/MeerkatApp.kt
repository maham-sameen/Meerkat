package com.example.meerkat

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.meerkat.feature_meetings.data.data_source.local.EmailWorker
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MeerkatApp
    @Inject constructor(): Application() , Configuration.Provider{
    @Inject
    lateinit var customWorkerFactory: CustomWorkerFactory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(customWorkerFactory)
            .build()


    }


class CustomWorkerFactory
@Inject
constructor(
    private val meetingRepo: MeetingRepo
) : WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker  = EmailWorker(
        context = appContext,
        workerParameters = workerParameters,
        meetingRepo = meetingRepo
    )
}