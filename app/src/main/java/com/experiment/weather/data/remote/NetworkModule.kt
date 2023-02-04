package com.experiment.weather.data.remote

import com.experiment.weather.common.utils.BASE_URL
import com.experiment.weather.data.remote.ktor.KtorApiService
import com.experiment.weather.data.remote.ktor.KtorServiceImpl
import com.experiment.weather.data.remote.retrofit.RetrofitApiService
import com.experiment.weather.data.remote.retrofit.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideKtorService(): KtorApiService {
        return KtorServiceImpl(
            client = HttpClient(Android) {
//                expectSuccess = true
//                useDefaultTransformers = true
//                followRedirects = true
                install(ContentNegotiation) {
                    json(
                        Json {
                            isLenient = true
                            prettyPrint = true
                        })
                }
            }
        )
    }

    @Singleton
    @Provides
    fun provideWeatherApi(
    ): RetrofitApiService {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideWeatherRemoteDataSource(
        remoteApi: RetrofitApiService ,
    ): WeatherRemoteDatasource = WeatherRemoteDatasource(remoteApi)


}