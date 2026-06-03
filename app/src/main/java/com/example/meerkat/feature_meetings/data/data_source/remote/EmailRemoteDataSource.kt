package com.example.meerkat.feature_meetings.data.data_source.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.meerkat.feature_meetings.data.data_source.local.EmailWorker
import com.example.meerkat.feature_meetings.data.data_source.util.ConstantsMeeting
import com.example.meerkat.feature_meetings.domain.model.EmailMessage
import com.example.meerkat.feature_meetings.domain.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.concurrent.TimeUnit



//data class EmailMessage(
//    val subject: String = "",
//    val html: String = ""
//)
//
//data class Email(
//    val to: String = "",
//    val message: EmailMessage = EmailMessage(),
//)


class EmailRemoteDataSource{


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendEmail(
        toSendEmail: String, message: Map<String , String>
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val mail = mapOf(
            "to" to listOf(toSendEmail),
            "message" to message
        )

        println("\uD83D\uDE00 [EmailRemoteDataSource] , sendEmail : ${toSendEmail.toString()}")

        firestore.collection("emails")
            .add(mail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("\uD83D\uDE00 [EmailRemoteDataSource] , sendEmail , email sent with styled HTML ")
                    Log.d("EmailTest", "✅ Email sent with styled HTML!")
                } else {
                    println("\uD83D\uDE00 [EmailRemoteDataSource] , sendEmail , email has not sent  ")
                    Log.e("EmailTest", "❌ Error sending email", task.exception)
                }
            }
    }



    fun sendEmailToWorker(
        emailToEmailMessage: List<EmailMessage>,
        workManager: WorkManager
    ){
        println("\uD83D\uDE00 [EmailRemoteDataSource] sendEmailToWorker , emailToEmailMessage : ${emailToEmailMessage.toString()}")
        val emailToEmailMessageJson = Gson().toJson(emailToEmailMessage)
        println("\uD83D\uDE00 [EmailRemoteDataSource] emailToEmailMessageJson : ${emailToEmailMessageJson.toString()}")
        val data = workDataOf(ConstantsMeeting.EMAIL_JSON to emailToEmailMessageJson)
        println("\uD83D\uDE00 [EmailRemoteDataSource] data : ${data.toString()}")
        val request = OneTimeWorkRequestBuilder<EmailWorker>()
            .setInputData(data)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL ,
                10 , TimeUnit.SECONDS
            )
            .build()
        workManager.enqueue(request)
    }
}