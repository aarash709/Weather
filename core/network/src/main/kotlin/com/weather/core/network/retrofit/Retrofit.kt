package com.weather.core.network.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.weather.core.network.BuildConfig.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


val moshi: Moshi = Moshi
    .Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

val retrofit: Retrofit? = Retrofit
    .Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

object WeatherApi{
    val retrofitService by lazy {
        retrofit?.create(RetrofitApiService::class.java)
    }
}
