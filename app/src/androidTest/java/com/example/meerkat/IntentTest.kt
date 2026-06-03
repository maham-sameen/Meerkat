package com.example.meerkat

import android.app.Activity.RESULT_OK
import android.app.Instrumentation.ActivityResult
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Test
import androidx.test.espresso.intent.Intents.intended
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules


@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class IntentTest {

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


    private fun pickImageIntent () : Matcher<Intent>{
        return allOf(
            hasAction(Intent.ACTION_GET_CONTENT),
            hasType("image/jpeg")
        )
    }

    private fun createGalleryPickActivityResultStub(): ActivityResult {
        val resources: Resources = composeTestRule.activity.resources
        val imageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    resources.getResourcePackageName(R.drawable.ic_launcher_background) + '/' +
                    resources.getResourceTypeName(R.drawable.ic_launcher_background) + '/' +
                    resources.getResourceEntryName(R.drawable.ic_launcher_background)
        )
        val resultIntent = Intent()
        resultIntent.setData(imageUri)
        return ActivityResult(RESULT_OK, resultIntent)
    }

    @Test
    fun test_validateIntentSentToPickPackage(){
        val expectedIntent = pickImageIntent()
        val activityResult = createGalleryPickActivityResultStub()
        intending(expectedIntent).respondWith(activityResult)
        composeTestRule.onNode(hasTestTag("pick_image")).performClick()
        intended(expectedIntent)
    }
}
/**
 *  What is ACTION_GET_CONTENT?
 * It's an Intent action that allows the user to select or create a piece of data (like an image,
 * video, document, or any file) and return it to your app.
 *
 * 🔸 Unlike ACTION_PICK, which selects existing data from a specific URI (like contacts from the
 * Contacts app),
 * 🔸 ACTION_GET_CONTENT allows the user to pick from multiple sources (including creating new content,
 * like taking a picture).
 *
 * 🔹 How does it work?
 * You launch an Intent with ACTION_GET_CONTENT, specifying the type of data you want. Android then
 * shows a list of apps that can provide that kind of content.
 *
 * Example 1: Selecting an image
 * Let's say your app needs an image from the user. You do this:
 *
 * val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
 *     type = "image/*"  // We want any type of image
 * }
 * startActivityForResult(intent, REQUEST_CODE)
 * 💡 What happens?
 *
 * Android shows all available apps that can provide an image (Gallery, Google Photos, File Manager, etc.).
 * The user picks an image.
 * Your app gets the selected image URI in onActivityResult().
 *
 *
 *
 *
 * intending :
 * This is a method used in UI testing when you need to test an activity that launches an intent and
 * expects a response. Instead of actually launching the new activity, you can fake (stub) the response
 * using this method.
 *
 * Why is this useful?
 * Sometimes, an activity sends an intent to another activity (even an external one) and expects
 * some result back. But in tests, we don’t want to actually open the new activity (because:
 *
 * It’s slow.
 * It may not exist in a test environment (especially if it’s an external app).
 * We just want to check if our activity handles the response correctly).
 * How does it work?
 * We use intending(matcher) to match the intent that we want to stub (fake a response for).
 * We use .thenRespond(myResponse) to define the fake response that should be returned when the
 * matched intent is triggered.
 * Now, when the test runs, the real activity is not launched, but our stubbed response is returned
 * instead.
 *
 *
 *
 * intended :
 * his method is used in UI testing to check that your app sends exactly one expected intent.
 *
 * It works just like verify(mock, times(1)) in Mockito, which checks that a method was called exactly
 * once.
 *
 * So here, it verifies that your app sent only one intent that matches a given condition.
 *
 * Why is this useful?
 * When testing an app that launches another activity or interacts with an external app, you want to
 * ensure that the intent was actually sent and not sent multiple times by mistake.
 *
 * Example scenario
 * Imagine you have a button in your app that should open the camera only once when clicked.
 *
 * If your app forgets to send the intent, the test should fail.
 * If your app accidentally sends it twice, the test should fail.
 * If your app sends it once (correctly), the test should pass.
 * How does it work?
 * Start recording intents: Call Intents.init() before your test runs. This makes Espresso start
 * tracking all intents your app sends.
 * Trigger the action in your app: This is usually clicking a button that sends an intent.
 * Verify the intent: Use intended(matcher) to check if the expected intent was sent exactly once.
 * Stop tracking: Call Intents.release() after your test is done to clean up.
 *
 *
 *
 *
 *
 * Breakdown of the Issue:
 * Your getIntent() function constructs an Intent for adding a Google account.
 * When you include Intent.createChooser(intent, "Choose an account"), it wraps the original
 * Intent inside a chooser.
 * Your authWithGoogleIntent() matcher expects an intent with ACTION_CHOOSER (which is created
 * using Intent.createChooser()).
 * When Intent.createChooser() is used, the outer intent has ACTION_CHOOSER, and the original
 * Settings.ACTION_ADD_ACCOUNT intent is inside EXTRA_INTENT.
 * Why VerificationModes.times(1) when Intent.createChooser() is included?
 * Your test expects an ACTION_CHOOSER intent.
 * Since Intent.createChooser() returns an intent with ACTION_CHOOSER, the matcher correctly
 * finds a match.
 * intended(expectedIntent, VerificationModes.times(1)) succeeds because the expected and actual
 * intents match.
 *
 *
 * Why VerificationModes.times(0) when Intent.createChooser() is removed?
 * If you remove Intent.createChooser(), the returned intent has only Settings.ACTION_ADD_ACCOUNT,
 * not ACTION_CHOOSER.
 * But your matcher still expects ACTION_CHOOSER, so the intent verification fails.
 * Since no matching intent is found, intended(expectedIntent, VerificationModes.times(0)) is true.
 *
 */