package com.experiment.weather

import android.app.Application
import androidx.core.view.WindowCompat
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        Timber.plant(Timber.DebugTree())
        Timber.e("this is a timber test in application class")
    }
}