package com.weather.sync.work

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.*
import com.weather.model.Coordinate
import com.weather.sync.work.utils.SyncManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class WorkSyncStatus @Inject constructor(
    @ApplicationContext private val context: Context,
) : SyncManager {
    override val isSyncing: Flow<Boolean> = WorkManager.getInstance(context)
        .getWorkInfosForUniqueWorkLiveData(WEATHER_FETCH_WORK_NAME)
        .asFlow()
        .map { workInfoList ->
            workInfoList.any {
                it.state == WorkInfo.State.RUNNING
            }
        }
        .catch {
            Timber.e("workInfos Error: ${it.message}")
        }

    override fun syncWithCoordinate(coordinate: Coordinate) {
        val workManager = WorkManager.getInstance(context)
        val constraints = Constraints(
            requiredNetworkType = NetworkType.CONNECTED,
        )
        val stringCoordinate = Json.encodeToString(coordinate)
        val workInputData = Data.Builder()
            .putString(WEATHER_COORDINATE, stringCoordinate).build()
        val fetchDataWorkRequest =
            OneTimeWorkRequestBuilder<FetchRemoteWeatherWorker>()
                .setConstraints(constraints)
                .setInputData(workInputData)
                .build()

        workManager.beginUniqueWork(
            WEATHER_FETCH_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            fetchDataWorkRequest
        ).enqueue()
    }
}