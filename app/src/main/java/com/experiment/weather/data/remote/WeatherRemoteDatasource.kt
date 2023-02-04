package com.experiment.weather.data.remote

import com.experiment.weather.common.utils.Resource
import com.experiment.weather.data.remote.dto.oncecall.OneCallDto
import com.experiment.weather.data.remote.model.geocode.GeoSearchItem
import com.experiment.weather.data.remote.retrofit.RetrofitApiService
import com.experiment.weather.repository.Coordinates
import kotlinx.coroutines.delay
import timber.log.Timber
import java.io.IOException

class WeatherRemoteDatasource(
    private val remoteApi: RetrofitApiService,
) {
    suspend fun getRemoteData(coordinates: Coordinates, exclude: String): Resource<OneCallDto> {
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
            Timber.e(e.message)
            Resource.Error(message = e.message.toString())
        }
    }

}