plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.daggerHiltPlugin)
    alias(libs.plugins.gmsGoogleServices)
}

android {
    namespace = "com.rodcollab.gymmateapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rodcollab.gymmateapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.rodcollab.gymmateapp.exercises.ExercisesDomainTest"

        vectorDrawables {
            useSupportLibrary = true
        }
    }
    testOptions {
        unitTests {
            unitTests.isReturnDefaultValues = true
            unitTests.isIncludeAndroidResources = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    implementation(libs.core.ktx)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.roboeletric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidTestRunner)
    androidTestImplementation(libs.android.test.core)
    androidTestImplementation(libs.android.test.rules)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.google.fonts)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.dagger.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    implementation(libs.navigation.compose)
    implementation(libs.navigation.compose.hilt)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    testImplementation(libs.mockito)
    testImplementation(libs.mockito.kotlin)

    testImplementation(libs.coroutines.test)

    androidTestImplementation(libs.dagger.hilt.test)

    implementation(libs.gson)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.worker.coroutines)
    implementation(libs.coroutines.core)
}