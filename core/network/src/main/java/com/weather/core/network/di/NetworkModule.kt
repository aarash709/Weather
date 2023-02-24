package com.weather.core.network.di

import com.weather.core.network.WeatherRemoteDatasource
import com.weather.core.network.retrofit.CommonValues.BASE_URL
import com.weather.core.network.retrofit.RetrofitApiService
import com.weather.core.network.retrofit.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    @Singleton
//    @Provides
//    fun provideKtorService(): KtorApiService {
//        return KtorServiceImpl(
//            client = HttpClient(Android) {
////                expectSuccess = true
////                useDefaultTransformers = true
////                followRedirects = true
//                install(ContentNegotiation) {
//                    json(
//                        Json {
//                            isLenient = true
//                            prettyPrint = true
//                        })
//                }
//            }
//        )
//    }

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
        remoteApi: RetrofitApiService,
    ): WeatherRemoteDatasource = WeatherRemoteDatasource(remoteApi)


}