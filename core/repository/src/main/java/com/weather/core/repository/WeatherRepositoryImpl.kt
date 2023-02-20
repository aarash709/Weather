package com.weather.core.repository

import com.weather.core.database.WeatherLocalDataSource
import com.weather.core.network.WeatherRemoteDatasource
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData
import com.weather.model.Resource
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteWeather: WeatherRemoteDatasource,
    private val localWeather: WeatherLocalDataSource,
) : WeatherRepository {
    override suspend fun deleteWeatherByCityName(cityName: String) {
        localWeather.deleteWeatherByCityName(cityName = cityName)
    }

    override fun isDatabaseEmpty(): Int = localWeather.databaseIsEmpty()

//    fun getWeatherPreviewByCityName(cityName: String, coordinates: Coordinates): Resource<Flow<WeatherData>>{
//        return remoteWeather.getRemoteData(cityName,coordinates)
//    }

    override fun getAllForecastWeatherData(): Flow<List<WeatherData>> {
        return localWeather.getAllForecastData()
    }


    override fun getAllWeatherLocations(): Flow<List<ManageLocationsData>> {
        return localWeather.getAllLocalWeatherData().map {
            it.map { data ->
                Timber.e("Saved Locations:${data.oneCall.cityName}")
                ManageLocationsData(
                    data.oneCall.cityName,
                    data.oneCall.lat.toString(),
                    data.oneCall.lon.toString(),
                    data.current.temp.toString(),
                    data.current.humidity.toString(),
                    data.current.feels_like.toString()
                )
            }
        }
    }

    override fun getLocalWeatherByCityName(cityName: String): Flow<WeatherData> {
        return localWeather.getLocalWeatherDataByCityName(cityName = cityName)
    }

    override suspend fun syncWeather(cityName: String, coordinate: Coordinate) {
        //work in progress
        val remoteWeatherInfo = remoteWeather.getRemoteData(
            coordinates = coordinate,
            exclude = "minutely"
        )
        Timber.e(remoteWeatherInfo.data?.timezone.toString())
        remoteWeatherInfo.let {
            localWeather.insertLocalData(
                oneCall = remoteWeatherInfo.data!!.toEntity(cityName = cityName),
                current = remoteWeatherInfo.data!!.current.toEntity(cityName = cityName),
                currentWeather = remoteWeatherInfo.data!!.current.weather.map {
                    it.toEntity(cityName = cityName)
                },
                daily = remoteWeatherInfo.data!!.daily.slice(0..3)
                    .map { it.toEntity(cityName = cityName) },
                hourly = remoteWeatherInfo.data!!.hourly.slice(0..12).map {
                    it.toEntity(cityName = cityName)
                }
            )
        }
    }

    override suspend fun syncWeather(coordinate: Coordinate) {
        try {
            val remoteWeatherInfo = remoteWeather.getRemoteData(
                coordinates = coordinate,
                exclude = "minutely"
            )
            Timber.e(remoteWeatherInfo.data?.timezone.toString())
            remoteWeatherInfo.let {
                localWeather.insertLocalData(
                    oneCall = remoteWeatherInfo.data!!.toEntity(cityName = coordinate.cityName.toString()),
                    current = remoteWeatherInfo.data!!.current.toEntity(cityName = coordinate.cityName.toString()),
                    currentWeather = remoteWeatherInfo.data!!.current.weather.map {
                        it.toEntity(cityName = coordinate.cityName.toString())
                    },
                    daily = remoteWeatherInfo.data!!.daily.slice(0..3)
                        .map { it.toEntity(cityName = coordinate.cityName.toString()) },
                    hourly = remoteWeatherInfo.data!!.hourly.slice(0..12).map {
                        it.toEntity(cityName = coordinate.cityName.toString())
                    }
                )
            }
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    override fun searchLocation(cityName: String): Flow<Resource<List<GeoSearchItem>>> =
        flow {
            emit(Resource.Loading())
            val remoteData = remoteWeather.directGeocode(cityName = cityName)
            if (remoteData.isNotEmpty())
                emit(Resource.Success(remoteData))
        }.catch {
            Timber.e(it.message)
            emit(Resource.Error(message = "Network Error: ${it.message}"))
        }
}