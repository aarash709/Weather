package com.weather.sync.work

import androidx.work.DelegatingWorkerFactory
import com.weather.core.repository.WeatherRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomWorkerFactory @Inject constructor(
    repository: WeatherRepository,
) : DelegatingWorkerFactory() {
    init {
        addFactory(WeatherWorkerFactory(repository))
        Timber.e("worker factory")
    }
}