package com.weather.sync.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

const val WEATHER_COORDINATE = "weatherCoordinate"

@HiltWorker
class FetchRemoteWeatherWorker @AssistedInject  constructor(
    @Assisted private val context: Context,
    @Assisted workParams: WorkerParameters,
    private val weatherRepository: WeatherRepository,
) : CoroutineWorker(appContext = context, params = workParams) {

    override suspend fun doWork(): Result {
        val coordinateString = inputData.getString(WEATHER_COORDINATE)
        val coordinate = Json.decodeFromString<Coordinate>(coordinateString.toString())
        return withContext(Dispatchers.IO) {
            syncWeather(coordinate = coordinate)
            Result.success()
        }
//        return Result.failure()
    }

    private suspend fun syncWeather(coordinate: Coordinate) {
        weatherRepository.syncWeather(coordinate = coordinate)
    }
}


const val WEATHER_FETCH_WORK_NAME = "weatherSyncWorkName"