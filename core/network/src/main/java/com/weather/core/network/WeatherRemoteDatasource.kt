package com.weather.core.network

import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.core.network.retrofit.RetrofitApiService
import com.weather.model.Coordinates
import com.weather.model.Resource
import com.weather.model.geocode.GeoSearchItem
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
            val geoData = remoteApi.getGeoSearch(location = cityName, limit = "5")
            Resource.Success(geoData.map { it.toGeoSearchItem() })
        }catch (e: Exception){
            Timber.e("direct geo error: ${e.message}")
            Resource.Error(message = e.message.toString())
        }
    }

}