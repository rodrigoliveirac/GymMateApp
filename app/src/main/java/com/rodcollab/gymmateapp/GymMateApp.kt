package com.rodcollab.gymmateapp

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.rodcollab.gymmateapp.worker.CoroutineDownloadWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GymMateApp : Application() {

    @Inject lateinit var auth: FirebaseAuth
    override fun onCreate() {
        super.onCreate()
        CoroutineDownloadWorker.schedule(this)
    }
}