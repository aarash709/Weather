package com.experiment.weather

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.weather.sync.work.CustomWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var customWorkerFactory: CustomWorkerFactory
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.e("this is a timber test in application class")
    }

    override val workManagerConfiguration: Configuration
        get() {
            Timber.e("this is a timber custom config")
            return Configuration.Builder()
                .setMinimumLoggingLevel(Log.ERROR)
                .setWorkerFactory(customWorkerFactory)
                .build()
        }

}
