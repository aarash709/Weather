package com.weather.feature.forecast.fake

import com.weather.model.Coordinate
import com.weather.sync.work.utils.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSyncManager : SyncManager {
    override val isSyncing: Flow<Boolean> = flow { emit(true) }

    override fun syncWithCoordinate(coordinate: Coordinate) {
        TODO("Not yet implemented")
    }
}