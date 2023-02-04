package com.experiment.weather.domain.di

import com.experiment.weather.domain.usecase.GeoSearch
import com.experiment.weather.domain.usecase.GetOneCall
import com.experiment.weather.domain.usecase.WeatherUseCases
import com.experiment.weather.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUseCase(
        repository: WeatherRepository,
    ): WeatherUseCases {
        return WeatherUseCases(
            geoSearch = GeoSearch(repository),
            getOneCall = GetOneCall(repository)
        )
    }

}