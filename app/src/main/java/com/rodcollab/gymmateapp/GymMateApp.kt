package com.rodcollab.gymmateapp

import android.app.Application
import com.rodcollab.gymmateapp.worker.CoroutineDownloadWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GymMateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineDownloadWorker.schedule(this)
    }
}