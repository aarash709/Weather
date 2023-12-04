package com.weather.core.network.di

import com.weather.core.network.util.ConnectivityMonitor
import com.weather.core.network.util.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBinding {

    @Binds
    abstract fun bindsNetworkManager(
        connectivityMonitor: ConnectivityMonitor
    ): NetworkManager

}