package com.weather.core.network.retrofit

import com.weather.core.network.BuildConfig.API_KEY
import com.weather.core.network.model.geosearch.GeoSearchItemDto
import com.weather.core.network.model.weather.NetworkOneCall
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApiService {

    @GET("data/2.5/onecall")
    suspend fun getOneCall(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude:String,
        @Query("appid") apiKey: String = API_KEY
    ): NetworkOneCall

    @GET("geo/1.0/direct")
//    @Api(BaseUrl.Geocode)
    suspend fun getGeoSearch(
        @Query("q") location: String,
        @Query("limit") limit: String,
        @Query("appid") appId: String = API_KEY
    ): List<GeoSearchItemDto>
}