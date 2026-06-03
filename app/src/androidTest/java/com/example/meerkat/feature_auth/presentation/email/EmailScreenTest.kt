package com.example.meerkat.feature_auth.presentation.email

import com.example.meerkat.di.clearAuthEmulator
import com.example.meerkat.di.clearFirestoreEmulator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class EmailScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp(){
        hiltRule.inject()
        runTest {
            FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)
            clearAuthEmulator(
                "10.0.2.2", 9099.toString()
            )
            // ✅ Firestore emulator on 8080
          Firebase.firestore.useEmulator("10.0.2.2", 8080)
           clearFirestoreEmulator(
               "10.0.2.2", 8080.toString()
           )
        }
    }


    @Test
    fun this_test()=runTest{

    }
}

