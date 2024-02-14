package com.experiment.weather.core.common.extentions.di

import com.experiment.weather.core.common.extentions.WeatherCoroutineDispatchers
import com.experiment.weather.core.common.extentions.WeatherDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @WeatherCoroutineDispatchers(WeatherDispatchers.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}

