package com.weather.core.network

import com.example.model.Coordinates
import com.example.model.Resource
import com.example.model.geocode.GeoSearchItem
import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.core.network.retrofit.RetrofitApiService
import kotlinx.coroutines.delay
import timber.log.Timber
import java.io.IOException

class WeatherRemoteDatasource(
    private val remoteApi: RetrofitApiService,
) {
    suspend fun getRemoteData(coordinates: Coordinates, exclude: String): Resource<NetworkOneCall> {
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

    suspend fun directGeocode(cityName: String): Resource<List<GeoSearchItem>> {
        return try {
            Resource.Loading(data = null)
            delay(1000)
            val geoData = remoteApi.getGeoSearch(location = cityName, limit = "5")
            Resource.Success(geoData.map { it.toGeoSearchItem() })
        }catch (e: Exception){
            Timber.e("direct geo error: ${e.message}")
            Resource.Error(message = e.message.toString())
        }
    }

}