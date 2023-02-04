package com.experiment.weather.data.remote.retrofit

import com.experiment.weather.common.utils.BASE_URL2
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshi: Moshi? = Moshi
    .Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

val retrofit: Retrofit? = Retrofit
    .Builder()
    .baseUrl(BASE_URL2)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

object WeatherApi{
    val retrofitService by lazy {
        retrofit?.create(RetrofitApiService::class.java)
    }
}
