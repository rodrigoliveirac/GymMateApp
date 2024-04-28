package com.rodcollab.gymmateapp

import com.google.firebase.auth.FirebaseAuth
import com.rodcollab.gymmateapp.auth.data.AuthRepositoryImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest

import org.junit.Test

import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@HiltAndroidTest
class AuthTest {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject lateinit var firestore: FirebaseAuth

    @Before
    fun setup() {
        hilt.inject()
    }

    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun createRepository() {
        repository = AuthRepositoryImpl(firestore)
    }

    @Test
    fun get() = runTest  {

    }

}