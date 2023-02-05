package com.weather.core.network.retrofit

import com.weather.core.network.model.geosearch.GeoSearchItemDto
import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.core.network.retrofit.CommonValues.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

object CommonValues{
    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = "e74687637f8aee445ebab69c4015f978"
}


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