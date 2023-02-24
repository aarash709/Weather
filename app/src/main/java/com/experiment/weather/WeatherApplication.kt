package com.experiment.weather

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.weather.sync.work.CustomWorkerFactory
import com.weather.sync.work.FetchRemoteWeatherWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var customWorkerFactory: CustomWorkerFactory
    override fun onCreate() {
        super.onCreate()
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        Timber.plant(Timber.DebugTree())
        Timber.e("this is a timber test in application class")
    }

    override fun getWorkManagerConfiguration(): Configuration {
        Timber.e("this is a timber custom config")

        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.ERROR)
            .setWorkerFactory(customWorkerFactory)
            .build()
    }
}
