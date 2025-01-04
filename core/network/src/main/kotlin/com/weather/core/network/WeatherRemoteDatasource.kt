package com.weather.core.network

import com.weather.core.network.BuildConfig.API_KEY
import com.weather.core.network.ktor.KtorApiService
import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.model.Coordinate
import com.weather.model.Resource
import com.weather.model.geocode.GeoSearchItem
import timber.log.Timber
import java.io.IOException

class WeatherRemoteDatasource(
//    private val remoteApi: RetrofitApiService,
    private val ktorApi: KtorApiService,

) {
//    suspend fun getRemoteData(coordinates: Coordinate, exclude: String): Resource<NetworkOneCall> {
//        return try {
//            val data = ktorApi.getOneCall(
//                lat = coordinates.latitude,
//                lon = coordinates.longitude
//            )
//            Resource.Success(data = data)
//        } catch (e: IOException) {
//            Timber.e("IO exeption ${e.message}")
//            Resource.Error(message = "Connectivity issue!")
//        }
//
//    }

    suspend fun directGeocode(cityName: String): List<GeoSearchItem> {
        return try {
            ktorApi.getGeoSearch(location = cityName, limit = "5", API_KEY).map {
                it.toGeoSearchItem()
            }
        } catch (e: Exception) {
            Timber.e("direct geo error: ${e.message}")
            return emptyList()
        }
    }

}