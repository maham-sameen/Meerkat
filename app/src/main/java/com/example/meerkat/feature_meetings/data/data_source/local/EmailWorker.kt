package com.example.meerkat.feature_meetings.data.data_source.local

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.meerkat.feature_meetings.data.data_source.util.ConstantsMeeting
import com.example.meerkat.feature_meetings.domain.model.EmailMessage
import com.example.meerkat.feature_meetings.domain.repository.MeetingRepo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class EmailWorker
@AssistedInject constructor(
    @Assisted  context : Context ,
    @Assisted workerParameters: WorkerParameters ,
    @Assisted private val meetingRepo: MeetingRepo
)  : CoroutineWorker(
    context , workerParameters
){
    override suspend fun doWork(): Result {

        println("\uD83D\uDE00 [EmailWorker] doWork()")
        val emailToEmailMessageJson = inputData.getString(ConstantsMeeting.EMAIL_JSON)  ?: return Result.failure()
        println("\uD83D\uDE00 [EmailWorker] emailToEmailMessageJson : ${emailToEmailMessageJson.toString()}")
        val type = TypeToken.getParameterized(List::class.java, EmailMessage::class.java).type  ?: return Result.failure()
        println("\uD83D\uDE00 [EmailWorker] type : ${type.toString()}")
        val emailToEmailMessage =  Gson().fromJson<List<EmailMessage>>(emailToEmailMessageJson, type) ?: return Result.failure()
        println("\uD83D\uDE00 [EmailWorker] emailToEmailMessage : ${emailToEmailMessage.toString()}")
        return try {
            println("\uD83D\uDE00 [EmailWorker] sending the email")
            meetingRepo.sendEmails(emailToEmailMessage)
            Result.success()
        }catch (e : Exception){
            println("\uD83D\uDE00 [EmailWorker] we have caught exception : ${e.message.toString()}")
            Result.failure()
        }
    }
}
