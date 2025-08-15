package com.example.vcquizo

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(PersistentCacheSettings
                .newBuilder()
                .setSizeBytes(100L * 1024 * 1024)
                .build())
            .build()

        FirebaseFirestore.getInstance().firestoreSettings = settings

    }

}