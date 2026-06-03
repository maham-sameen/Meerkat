package com.example.meerkat

import android.content.Intent
import android.provider.Settings
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@HiltAndroidTest
class FirebaseTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>() s

    @get:Rule(order = 1)
    val watchman = object : TestWatcher() {
        override fun starting(description: Description) {
            if(description.methodName.toString() == "authWithGoogle_alreadyHaveAccount"){
                FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)
            }else{
                Firebase.firestore.useEmulator("10.0.2.2", 8080)
            }
        }

        override fun succeeded(description: Description) {

        }

        override fun failed(e: Throwable, description: Description) {

        }
    }

    @Before
    fun setUp(){
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun tearDown(){
        Intents.release()
    }

    @Test
    fun signUp() = runTest {
        val result = async {
            signUpWithEmail("mahamsameenhere@gmail.com", "apa_PHER_milangan@2times")
        }
        println("sign up system${result.await().toString()}")
    }


    @Test
    fun registerUser()  = runTest {
        val result = async {
            registerUser(
                User(
                    id = "123",
                    name = "maham",
                    email = "mahamsameenhere@gmail.com",
                    image = "",
                    mobileNumber = 3276010123,
                    fcmToken = ""
                )
            )
        }
        println("register user ${result.await().toString()}")
    }
    @Test
    fun authWithGoogle_alreadyHaveAccount() = runTest {
        try {
            // Simulate a valid token structure for Google Sign-In
            val fakeToken =
                """{"sub": "apa_PHER_milangan@2times", "email": "mahamsameenhere@gmail.com", "email_verified": true}"""
            val credential = GoogleAuthProvider
                .getCredential(fakeToken, null)

            println("credential : ${credential.toString()}")
            val authResult = FirebaseAuth
                .getInstance()
                .signInWithCredential(credential).await()

            println("authResult : ${authResult.user.toString()}")
            println("User signed in: ${authResult.user?.email}")

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            println("FirebaseAuthInvalidCredentialsException: ${e.message}")
        } catch (e: FirebaseApiNotAvailableException) {
            println("FirebaseApiNotAvailableException: ${e.message}")
        } catch (e: FirebaseAuthInvalidUserException) {
            println("FirebaseAuthInvalidUserException: ${e.message}")
        } catch (e: Exception) {
            println("Unexpected error: ${e.message}")
        }
    }

    @Test
    fun sign_out() = runTest {
        val context = composeTestRule.activity.applicationContext
        val googleSignInClient = GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        )
        try {
            googleSignInClient.signOut().await()

            // Check if the user is actually signed out
            val account = GoogleSignIn.getLastSignedInAccount(context)
            assertNull("User is still signed in after sign out", account)
        } catch (e: Exception) {
            fail("Unexpected error: ${e.message}")
        }
    }


    @Test
    fun authWithGoogle_noAccountExist(){

    }

    private fun authWithGoogleIntent(): Matcher<Intent> {
        return allOf(
            hasAction(Settings.ACTION_ADD_ACCOUNT),
            hasExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        )
    }
}
/**
* Sign up a new user with email and password
*/
suspend fun signUpWithEmail(email: String, password: String): String {
    return try {
        val result =
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: return "Error creating account. Please try again."
        user.toString()
    } catch (e: Exception) {
        if (e is FirebaseAuthUserCollisionException) {
            "This email is already in use. Please sign in instead."
        }
        if (e is CancellationException) throw e
        with(NonCancellable) { e.localizedMessage ?: "Unknown error occurred." }
    }
}


suspend fun registerUser(
    userInfo: User
) : String {

    return  try {
         FirebaseFirestore.getInstance().collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge()).await()
        "successfully saved the user"
    }catch (e : Exception){
        e.message.toString()
    }
}



data class User(
    val id : String = "",
    val name : String = "",
    val email : String  = "",
    val image : String ="",
    val mobileNumber: Long = 0,
    val fcmToken : String = "",
)

//https://stackoverflow.com/questions/19748587/cant-sign-up-for-azure-we-were-unable-to-verify-your-account


