package com.weather.sync.work.utils

import com.weather.model.Coordinate
import kotlinx.coroutines.flow.Flow


interface SyncManager {
    val isSyncing : Flow<Boolean>

    fun syncWithCoordinate(coordinate: Coordinate)
}