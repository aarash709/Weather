package com.weather.core.network.di

import com.weather.core.network.WeatherRemoteDatasourceImpl
import com.weather.core.network.ktor.KtorApiService
import com.weather.core.network.ktor.KtorServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideKtorService(): KtorApiService {
        return KtorServiceImpl(
            client = HttpClient(Android) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            isLenient = true
                            prettyPrint = true
                            ignoreUnknownKeys = true
                        })
                }
            }
        )
    }

    @Singleton
    @Provides
    fun provideWeatherRemoteDataSource(
        ktor: KtorApiService,
    ): WeatherRemoteDatasourceImpl = WeatherRemoteDatasourceImpl(ktorApi = ktor )


}