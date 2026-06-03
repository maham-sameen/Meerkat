package com.example.meerkat

import android.app.Activity.RESULT_OK
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.provider.Settings
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.VerificationModes
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class AuthIntentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this) // Ensure Hilt initializes first

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private fun authWithGoogleIntent(): Matcher<Intent> {
//        return allOf(
//            hasAction(Intent.ACTION_CHOOSER), // Adjust based on getIntent() output
//            hasExtra(Intent.EXTRA_INTENT, allOf(
//                hasAction(Settings.ACTION_ADD_ACCOUNT),
//                hasExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
//            ))
//        )

        return allOf(
                hasAction(Settings.ACTION_ADD_ACCOUNT),
                hasExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            )
    }

    @Test
    fun authWithGoogle_noAccountExist() {
        val expectedIntent = authWithGoogleIntent()

        intending(expectedIntent).respondWith(ActivityResult(RESULT_OK, Intent()))
        // Perform button click
        composeTestRule.onNode(hasTestTag("click_me"))
            .assertExists()
            .performClick()

        // Verify that the expected intent was never sent
        intended(expectedIntent, VerificationModes.times(1))
    }

}
/**
 *
 * ACTION_CHOOSER
 *
 * What is an Activity Chooser?
 * An Activity Chooser is a special dialog that lets the user pick from multiple apps that
 * can handle a particular action. It’s useful when the system finds multiple matching apps
 * for an intent.
 *
 * Example Scenario
 * Suppose you have an image in your app and you want the user to pick an app to share it.
 * Multiple apps like Gmail, WhatsApp, and Messages can handle this action. Instead of letting
 * Android auto-pick one, you show an Activity Chooser so the user can choose.
 *
 * Key Features of an Activity Chooser
 *
 * Custom Title
 * Unlike the default system chooser, you can specify a custom title like "Choose an app
 * to share" instead of a generic system message.
 *
 * No "Set as Default" Option
 * Normally, when Android shows a picker, users can select "Always" or "Just Once."
 * In the Activity Chooser, the user must choose every time—there’s no option to set a
 * preferred/default app.
 *
 * All Apps Always Shown
 * Even if a user previously selected an app as preferred, it won't be automatically
 * chosen. The chooser will always appear.
 *
 * When to Use an Activity Chooser?
 * Use it when the user is expected to choose an app explicitly.
 *
 *
 * ✅ Good Use Cases:
 *
 * Sharing an image, document, or link to different apps (e.g., Gmail, WhatsApp, Telegram).
 * Opening a file in different available apps.
 * Letting the user choose between multiple navigation apps (e.g., Google Maps, Waze).
 *
 * 🚫 When NOT to Use It:
 * 1️⃣ The Key Idea: When to Use an Activity Chooser vs. Direct Intent
 * The difference depends on what the user naturally expects when performing an action.
 *
 * 🚫 When NOT to Use an Activity Chooser
 * If the user expects an app to open directly, do not use the chooser. Instead, call startActivity(intent).
 *
 * 🔹 Example: Clicking a "mailto:" link
 *
 * Suppose a user clicks on a "Contact Us" email link in your app.
 * The user expects their default email app to open immediately.
 * If they don’t have a default, Android will show a system dialog asking which app to use.
 * You should use startActivity(intent) directly.
 *
 * ✅ When to Use an Activity Chooser
 * If the user needs to choose between multiple actions, use Intent.createChooser(). This ensures:
 *
 * The user always gets a choice.
 * A custom title is displayed (e.g., "Send this photo with:").
 * The user cannot set a default app (they must choose every time).
 * 🔹 Example: Sending a Picture
 *
 * The user taps a "Share" button while viewing a photo.
 * They might want to:
 * Send via email 📧
 * Share on WhatsApp 📱
 * Post on Instagram 📸
 * Upload to Google Drive ☁️
 * Here, a chooser is the best option because:
 * There are multiple possible actions.
 * The user may want to use a different app each time.
 *
 *
 * 1️⃣ The Problem: Granting URI Permissions
 * When sharing a file or content URI (like an image, PDF, or video) with another app, you must grant it temporary permission to access that file.
 *
 * 🔴 Why?
 * Android enforces strict file access rules.
 * Apps cannot access files from another app unless they have the necessary permission.
 * If you only pass a URI in the intent without permissions, the target app might fail to
 * open the file.
 *
 * ✅ Solution
 * You need to:
 *
 * Attach the URI properly using setClipData().
 * Grant temporary read/write permissions using:
 * FLAG_GRANT_READ_URI_PERMISSION (if the other app only needs to read the file)
 * FLAG_GRANT_WRITE_URI_PERMISSION (if the other app needs to modify the file)
 *
 * 2️⃣ How This Works with Activity Chooser
 * If you're using Intent.createChooser(), you must set permissions on both:
 *
 * The main chooser Intent (ACTION_CHOOSER)
 * The inner target Intent (EXTRA_INTENT)
 *
 * 🚨 Why?
 * When using a chooser, Android wraps your intent inside another intent.
 * If you only set permissions on the inner intent, they won’t be applied correctly when
 * the chooser launches.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * Add Account
 * 1️⃣ What is ACTION_ADD_ACCOUNT?
 * It's a built-in Android intent action that opens the "Add Account" screen, allowing the
 * user to add a new account (like Google, Facebook, or custom accounts).
 * 3️⃣ Filtering Accounts Using Extras
 * Sometimes, you don’t want to show all possible accounts, just specific ones. Android provides two extras to filter them:
 *
 * 🔹 EXTRA_AUTHORITIES
 * Used to show only accounts that can sync with a specific content provider.
 * Example: If you pass ContactsContract.AUTHORITY, only accounts that sync contacts will be shown.
 * 🔹 EXTRA_ACCOUNT_TYPES
 * Used to restrict the list to specific account types.
 * Example: If you only want to show Google accounts, you can do this:
 * Intent intent = new Intent("android.settings.ADD_ACCOUNT_SETTINGS");
 * intent.putExtra("EXTRA_ACCOUNT_TYPES", new String[]{"com.google"});
 * startActivity(intent);
 * 4️⃣ What if No Activity Supports This?
 * Not all devices support this intent, so always use resolveActivity() before starting it:
 *
 * Intent intent = new Intent("android.settings.ADD_ACCOUNT_SETTINGS");
 * if (intent.resolveActivity(getPackageManager()) != null) {
 *     startActivity(intent);
 * } else {
 *     Toast.makeText(this, "No supported activity found!", Toast.LENGTH_SHORT).show();
 * }
 */