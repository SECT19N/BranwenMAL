package com.branwen.mal

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Custom [Application] class for the Branwen Mal app.
 *
 * This class is annotated with [HiltAndroidApp] to enable Hilt dependency injection
 * throughout the application.
 *
 * In debug builds, it initializes Timber for enhanced logging.
 */
@HiltAndroidApp
class BranApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}