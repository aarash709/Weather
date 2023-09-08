package com.experiment.weather.core.common.extentions.di

import com.experiment.weather.core.common.extentions.Dispachers
import com.experiment.weather.core.common.extentions.WeatherDidpatchers
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
    @Dispachers(WeatherDidpatchers.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}

