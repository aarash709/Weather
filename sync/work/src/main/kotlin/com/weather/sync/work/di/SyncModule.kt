package com.weather.sync.work.di

import com.weather.sync.work.WorkSyncStatus
import com.weather.sync.work.utils.SyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SyncModule {
    @Binds
    fun bindsSyncManager(
        syncStatus: WorkSyncStatus
    ): SyncManager
}