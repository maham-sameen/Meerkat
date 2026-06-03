

import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)

    id("kotlin-parcelize")

}



android {
      namespace = "com.example.meerkat"
      compileSdk = 36

     defaultConfig {
        applicationId = "com.example.meerkat"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.meerkat.HiltTestRunner"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.example.meerkat"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
            freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true

    }

    testOptions {
        packaging {
            resources.excludes.add("META-INF/*")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.adapters)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)





    // coroutine
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    implementation(libs.play.services.auth)


    // Room
    implementation (libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Kotlin Extensions and Coroutines support for Room
    implementation (libs.androidx.room.ktx)



    // Compose dependencies
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation( libs.androidx.navigation.compose)
    //implementation (libs.androidx.material.icons.extended)
    implementation( libs.androidx.hilt.navigation.compose)


    // Local unit tests
    testImplementation( libs.androidx.core)
    testImplementation (libs.junit)
    testImplementation (libs.androidx.core.testing)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation (libs.mockwebserver)
    testImplementation (libs.mockk)
    debugImplementation(libs.ui.test.manifest)

    // Instrumentation tests
    androidTestImplementation (libs.hilt.android.testing)
    // ...with Kotlin.
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation (libs.junit)
    androidTestImplementation (libs.kotlinx.coroutines.test)
    androidTestImplementation (libs.androidx.core.testing.v210)
    androidTestImplementation (libs.truth.v113)
    androidTestImplementation (libs.androidx.junit.v113)
    androidTestImplementation (libs.core.ktx)
    androidTestImplementation (libs.mockwebserver)
    androidTestImplementation (libs.mockk.android)
    androidTestImplementation (libs.androidx.runner)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)


    // mockito - kotlin
    // required if you want to use Mockito for unit tests
    testImplementation (libs.mockito.core)
    // required if you want to use Mockito for Android tests
    androidTestImplementation (libs.mockito.android)
    testImplementation(libs.mockito.kotlin)


    // turbine
    testImplementation(libs.turbine)





    //FIREBASE
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.firestore)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.play.services.auth)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)


    implementation (libs.androidx.credentials)
    implementation( libs.androidx.credentials.play.services.auth)
    implementation (libs.googleid)

    // espresso
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.intents)

    // coil
    implementation(libs.coil.compose)


    // splash screen
    implementation(libs.androidx.core.splashscreen)



    //type safe navigation
    implementation(libs.kotlinx.serialization.json)

    // country or code picker
    implementation(libs.androidx.material)
    implementation(libs.jetpack.compose.country.code.picker.emoji)



    //Network calls
    implementation(libs.retrofit)

    //Json to Kotlin object mapping
    implementation(libs.converter.gson)

    // material 3
    implementation (libs.material3)

    // calender
    implementation(libs.calendar)

    implementation (libs.datetime)
    coreLibraryDesugaring (libs.desugar.jdk.libs)



    // OAuth
    implementation (libs.jwtdecode)
    implementation (libs.appauth)


    //data store
    implementation(libs.androidx.datastore.preferences)





    //paging
    implementation(libs.paging.compose)
    implementation(libs.room.paging)



    // google function
    implementation (libs.firebase.functions.ktx)


    implementation(libs.google.firebase.functions.ktx)





    // coroutine worker
    // work manager with coroutine
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    testImplementation(kotlin("test"))


    // for loging
    implementation("com.jakewharton.timber:timber:5.0.1")

}