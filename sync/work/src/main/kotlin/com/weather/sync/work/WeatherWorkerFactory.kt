package com.weather.sync.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.weather.core.repository.WeatherRepository
import dagger.Provides
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherWorkerFactory @Inject constructor(
    private val repository: WeatherRepository,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            FetchRemoteWeatherWorker::class.java.name ->
                FetchRemoteWeatherWorker(context = appContext, workerParameters, repository)

            else -> null
        }
    }
}