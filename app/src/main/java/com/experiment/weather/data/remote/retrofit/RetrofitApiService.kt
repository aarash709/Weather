package com.experiment.weather.data.remote.retrofit

import com.experiment.weather.common.utils.API_KEY
import com.experiment.weather.data.remote.dto.geoSearch.GeoSearchItemDto
import com.experiment.weather.data.remote.dto.oncecall.OneCallDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApiService {

    @GET("data/2.5/onecall")
    suspend fun getOneCall(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude:String,
        @Query("appid") apiKey: String = API_KEY
    ): OneCallDto

    @GET("geo/1.0/direct")
//    @Api(BaseUrl.Geocode)
    suspend fun getGeoSearch(
        @Query("q") location: String,
        @Query("limit") limit: String,
        @Query("appid") appId: String = API_KEY
    ): List<GeoSearchItemDto>
}