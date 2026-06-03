package com.example.meerkat.di.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.example.meerkat.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.meerkat.R


const val MAIN_CHANNEL_ID = "Main Channel ID"
const val MAIN_CHANNEL = "Main Channel"


/**
 * Absolutely bro! Let's break down both terms **simply and clearly** so the full concept becomes crystal clear for you. 💡
 *
 * ---
 *
 * ## 🔷 1. `NotificationCompat`
 *
 * ### 📌 What is it?
 *
 * `NotificationCompat` is a **helper class** from the Android **Jetpack library** that helps you **build notifications** that work across **all Android versions** (old and new).
 *
 * ---
 *
 * ### 🤔 Why use `NotificationCompat.Builder`?
 *
 * Because writing notifications directly with platform APIs can cause problems on older devices. `NotificationCompat.Builder` ensures that your code:
 *
 * ✅ Works on Android 5.0, 6.0, 7.0... all the way up to Android 14+
 * ✅ Uses the best features supported on that Android version
 *
 * ---
 *
 * ### 🧱 Example:
 *
 * ```kotlin
 * val builder = NotificationCompat.Builder(context, "channel_id")
 *     .setContentTitle("Hello!")
 *     .setContentText("This is a message.")
 *     .setSmallIcon(R.drawable.ic_notification)
 *     .build()
 * ```
 *
 * This is how you **build** the notification.
 *
 * ---
 *
 * ## 🔷 2. `NotificationManagerCompat`
 *
 * ### 📌 What is it?
 *
 * `NotificationManagerCompat` is another Jetpack helper class. It's used to **show (post)** the notification that you created with `NotificationCompat`.
 *
 * It acts like a **safe wrapper** around the Android system's `NotificationManager`.
 *
 * ---
 *
 * ### 🔔 Why use `NotificationManagerCompat`?
 *
 * Because:
 * ✅ It works safely across all Android versions
 * ✅ It gives extra helper methods like `.areNotificationsEnabled()` to check if the user has blocked notifications
 *
 * ---
 *
 * ### 📤 Example:
 *
 * ```kotlin
 * NotificationManagerCompat.from(context).notify(1, notification)
 * ```
 *
 * This will actually **display the notification** to the user.
 *
 * ---
 *
 * ## 🧠 Summary Table:
 *
 * | Term                         | What it Does            | Simple Meaning           |
 * | ---------------------------- | ----------------------- | ------------------------ |
 * | `NotificationCompat.Builder` | Builds the notification | Like writing a message   |
 * | `NotificationManagerCompat`  | Shows the notification  | Like sending the message |
 *
 * ---
 *
 * ## 🔁 Real-Life Example Flow:
 *
 * 1. ✅ **Build** the notification:
 *
 * ```kotlin
 * val notification = NotificationCompat.Builder(context, "channel_id")
 *     .setContentTitle("Hey!")
 *     .setContentText("This is a test.")
 *     .setSmallIcon(R.drawable.ic_notification)
 *     .build()
 * ```
 *
 * 2. 📤 **Show** it:
 *
 * ```kotlin
 * NotificationManagerCompat.from(context).notify(1001, notification)
 * ```
 *
 * Done!
 *
 * ---
 *
 * Let me know if you want a full working example in a real app setup, bro!
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    /**
     * Sure bro! Here's a **clear and short** explanation:
     *
     * ---
     *
     * ### 🔒 `.setVisibility(VISIBILITY_PRIVATE)`
     *
     * This is used when you're creating a **notification**, and it controls **how much of the notification is shown on the lock screen**.
     *
     * ---
     *
     * ### 🔐 `VISIBILITY_PRIVATE` means:
     *
     * * On the **lock screen**, the **sensitive content is hidden**.
     * * It may show a message like: **"New message"** instead of the actual text like **"Hey bro, what's up?"**
     *
     * This protects user **privacy**.
     *
     * ---
     *
     * ### 📱 Where it’s used?
     *
     * ```kotlin
     * builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
     * ```
     *
     * Use this when your notification has **personal or sensitive data**, like:
     *
     * * Messages
     * * OTPs
     * * Bank alerts
     *
     * ---
     *
     * ### 🔁 Other options for visibility:
     *
     * | Value                | What it shows on lock screen  |
     * | -------------------- | ----------------------------- |
     * | `VISIBILITY_PUBLIC`  | Shows full content            |
     * | `VISIBILITY_PRIVATE` | Hides sensitive content       |
     * | `VISIBILITY_SECRET`  | Hides notification completely |
     *
     * ---
     *
     * Let me know if you want an example code, bro!
     *
     * Sure bro! Here's a **simple and clear explanation** of what this code does:
     *
     * ---
     *
     * ### 🔒 What is `.setPublicVersion(...)`?
     *
     * This is used to **customize what the notification shows on the lock screen** when the notification visibility is set to `VISIBILITY_PRIVATE`.
     *
     * ---
     *
     * ### 📱 Why use it?
     *
     * If your notification contains **private or sensitive info** (like a chat message), you may not want to show the full message on the lock screen.
     *
     * So instead of just hiding content, you can **show a safe version** using `.setPublicVersion(...)`.
     *
     * ---
     *
     * ### 🧠 What this code does:
     *
     * ```kotlin
     * .setPublicVersion(
     *     NotificationCompat.Builder(context, "Main Channel ID")
     *         .setContentTitle("Hidden")
     *         .setContentText("Unlock to see the message.")
     *         .build()
     * )
     * ```
     *
     * This means:
     *
     * 👉 On the **lock screen**, it will show:
     *
     * * Title: `Hidden`
     * * Text: `Unlock to see the message.`
     *
     * 👉 When the phone is **unlocked**, the **real notification content** will appear.
     *
     * ---
     *
     * ### ✅ Full Concept Recap:
     *
     * * `VISIBILITY_PRIVATE` hides sensitive content on lock screen.
     * * `.setPublicVersion()` lets you **show a custom safe version** instead of a blank or generic one.
     * * User unlocks the phone → sees full details.
     *
     * ---
     *
     * Let me know if you want a full working example too, bro!
     *
     */
    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder{
        val activityIntent = Intent(context , MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0 , PendingIntent.FLAG_IMMUTABLE)
        }
       return NotificationCompat.Builder(
            context ,
            MAIN_CHANNEL_ID
        ).setContentTitle("Meeting has Scheduled")
            .setSmallIcon(R.drawable.darklogo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setPublicVersion(
                NotificationCompat.Builder(
                    context ,
                    MAIN_CHANNEL_ID
                ).setContentTitle("Hidden")
                    .setContentText("Unlock to see event status")
                    .build()
            )
//           .setAutoCancel(true)
//           .setContentIntent(pendingIntent)


    }

    /**
     * Great question, bro! Let me explain it **clearly and simply**:
     *
     * ---
     *
     * ### 🤔 Why do we check the version like this?
     *
     * ```kotlin
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
     * ```
     *
     * This line means:
     *
     * > "Only run the code inside if the Android version is **Android 8.0 (API 26)** or **above**."
     *
     * ---
     *
     * ### 📱 Why Android 8.0 (API 26) is special?
     *
     * Starting from **Android 8.0**, Google **made it mandatory** to use **notification channels**.
     *
     * 🔴 If you **don’t create a channel** on Android 8.0+ → your **notification won’t show**!
     *
     * But on Android **7.1 and below**, channels **don’t exist** — and trying to create one would **crash the app**.
     *
     * ---
     *
     * ### ✅ So the version check is used to:
     *
     * | Purpose                            | Why it's needed                                                   |
     * | ---------------------------------- | ----------------------------------------------------------------- |
     * | ✅ Prevent crash                    | On Android 7.1 and below, there’s no `NotificationChannel` class. |
     * | ✅ Only use channels where required | Android 8.0+ requires channels to show notifications.             |
     * | ✅ Support all versions             | So your app works on both old and new Android devices.            |
     *
     * ---
     *
     * ### 🧠 Real-life example:
     *
     * | Android Version      | What happens                                            |
     * | -------------------- | ------------------------------------------------------- |
     * | Android 7.1 or below | Skip channel creation — just show notification directly |
     * | Android 8.0 or above | Must create a channel, or notification won't appear     |
     *
     * ---
     *
     * ### 🔁 Summary
     *
     * We check version like this:
     *
     * ```kotlin
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
     * ```
     *
     * ...to **only create a channel** on Android 8.0 or above — because only those versions **need** it and **support** it.
     *
     * ---
     *
     * Let me know if you want a version check chart or visual diagram too, bro!
     *
     */
    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ) : NotificationManagerCompat{
        val notificationManager = NotificationManagerCompat.from(context)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                MAIN_CHANNEL_ID ,
                MAIN_CHANNEL  ,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }
}