package com.example.meerkat.feature_auth.domain.use_case.auth

import com.example.meerkat.di.clearAuthEmulator
import com.example.meerkat.feature_auth.data.data_source.remote.EmailClientUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@HiltAndroidTest
class SignUpEmailTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var emailClientUtils: EmailClientUtils

    @get:Rule(order = 1)
    val watchman = object : TestWatcher(){
        override fun starting(description: Description?) {
            val host = "10.0.2.2"
            val port = 9099
            FirebaseAuth.getInstance().useEmulator(host, port)
            runTest{ clearAuthEmulator(host, port.toString()) }
        }
    }


    @Before
    fun setUp(){
        hiltRule.inject()
        emailClientUtils = EmailClientUtils()
    }

    // 🔹 Helper: create a valid user
    private suspend fun createTestUser(email: String, password: String) {
        val result = emailClientUtils.signUpWithEmail(email, password)
        Assert.assertNull(result.errorMessage)
        Assert.assertTrue(result.data!= null)
    }


    @Test
    fun user_correct_email_password_signUp_successfully()= runTest{
        val email = "mahamsameenhere@gmail.com"
        val password = "apa_PHER_milangan@2times"
        val authResult =  emailClientUtils.signUpWithEmail(email,password)
        Assert.assertNull(authResult.errorMessage)
        assert(authResult.data != null)
        assert(authResult.data?.email == email )
    }

    @Test
    fun user_correct_email_signIn_successfully()=runTest{
        val email = "mahamsameenhere@gmail.com"
        val password = "apa_PHER_milangan@2times"
        createTestUser(email , password)
        val result = emailClientUtils.signInWithEmail(email , password)
        Assert.assertTrue(result.isSuccess)
        assert(result.getOrNull() != null)
    }

    @Test
    fun user_wrong_email_signIn_failed()=runTest {
        val email = "maham"
        val password = "apa_PHER_milangan@2times"
        val correctEmail = "mahamsameenhere@gmail.com"
        createTestUser(correctEmail , password)
        val result =  emailClientUtils.signInWithEmail(email,password)
        Assert.assertTrue(result.isFailure)
        assert(result.exceptionOrNull() is FirebaseAuthInvalidCredentialsException)
    }
    @Test
    fun user_wrong_password_signIn_failed()=runTest {
        val email = "mahamsameenhere@gmail.com"
        val password = "apa"
        val correctPassword = "apa_PHER_milangan@2times"
        createTestUser(email , correctPassword)
        val result =  emailClientUtils.signInWithEmail(email,password)
        Assert.assertTrue(result.isFailure)
        assert(result.exceptionOrNull() is FirebaseAuthInvalidCredentialsException)
    }
    @Test
    fun user_wrong_email_signUp_failed()=runTest {
        val email = "maham"
        val password = "apa_PHER_milangan@2times"
        val authResult =  emailClientUtils.signUpWithEmail(email,password)
        assert(authResult.errorMessage != null)
        Assert.assertNull(authResult.data)
    }
    @Test
    fun user_wrong_password_signUp_failed()=runTest {
        val email = "mahamsameenhere@gmail.com"
        val password = "apa"
        val authResult =  emailClientUtils.signUpWithEmail(email,password)
        assert(authResult.errorMessage != null)
        Assert.assertNull(authResult.data)
    }
}