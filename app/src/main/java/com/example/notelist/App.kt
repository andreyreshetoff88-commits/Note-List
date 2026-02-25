package com.example.notelist

import android.app.Application
import com.cloudinary.android.MediaManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val config = mapOf(
            "cloud_name" to "dc6roc3dn"
        )

        MediaManager.init(this, config)
    }
}