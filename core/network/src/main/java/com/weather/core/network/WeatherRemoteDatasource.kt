package com.weather.core.network

import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.core.network.retrofit.RetrofitApiService
import com.weather.model.Coordinate
import com.weather.model.Resource
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.delay
import timber.log.Timber
import java.io.IOException

class WeatherRemoteDatasource(
    private val remoteApi: RetrofitApiService,
) {
    suspend fun getRemoteData(coordinates: Coordinate, exclude: String): Resource<NetworkOneCall> {
        return try {
            val data = remoteApi.getOneCall(
                lat = coordinates.latitude,
                lon = coordinates.longitude,
                exclude = exclude
            )
//            Timber.e(data.timezone)
            Resource.Success(data = data)
        } catch (e: IOException) {
            Timber.e("IO exeption ${e.message}")
            Resource.Error(message = "Connectivity issue!")
        }

    }

    suspend fun directGeocode(cityName: String): List<GeoSearchItem> {
        return try {
            remoteApi.getGeoSearch(location = cityName, limit = "5").map {
                it.toGeoSearchItem()
            }
        } catch (e: Exception) {
            Timber.e("direct geo error: ${e.message}")
            return emptyList()
        }
    }

}