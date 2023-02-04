package com.experiment.weather.repository

import com.experiment.weather.common.utils.Resource
import com.experiment.weather.data.database.WeatherLocalDataSource
import com.experiment.weather.data.remote.WeatherRemoteDatasource
import com.experiment.weather.data.remote.model.ManageLocationsData
import com.experiment.weather.data.remote.model.geocode.GeoSearchItem
import com.experiment.weather.data.remote.model.weatherData.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

data class Coordinates(
    val latitude: String,
    val longitude: String,
)

class WeatherRepositoryImpl @Inject constructor(
    private val remoteWeather: WeatherRemoteDatasource,
    private val localWeather: WeatherLocalDataSource,
) : WeatherRepository {

    override fun databaseIsEmpty(): Int = localWeather.databaseIsEmpty()

    override fun getAllForecastWeatherData(): Flow<List<WeatherData>> {
        return localWeather.getAllLocalWeatherData().map {
            it.map {
                Timber.e("forecast: ${it.oneCall.cityName}")
                WeatherData(
                    coordinates = it.oneCall.asDomainModel(),
                    current = it.current.asDomainModel(
                        weather = emptyList()
                    )
                )
            }
        }
    }


    override fun getAllLocationsWeatherData(): Flow<List<ManageLocationsData>> {
        return localWeather.getAllLocalWeatherData().map {
            it.map {
                Timber.e("Saved Locations:${it.oneCall.cityName}")
                ManageLocationsData(
                    it.oneCall.cityName,
                    it.current.temp.toString(),
                    it.current.humidity.toString(),
                    it.current.feels_like.toString()
                )
            }
        }
    }

    override fun weatherLocalDataStream(cityName: String): Flow<WeatherData> {
        return localWeather.getLocalData(cityName = cityName)

    }

    override suspend fun syncLatestWeather(cityName: String, coordinates: Coordinates) {
        //work in progress
        val remoteWeatherInfo = remoteWeather.getRemoteData(
            coordinates = coordinates,
            exclude = "minutely"
        )
        Timber.e(remoteWeatherInfo.data?.timezone.toString())
        if (remoteWeatherInfo.data != null)
            localWeather.insertLocalData(cityName, remoteWeatherInfo.data)
    }

    override suspend fun searchLocation(cityName: String): Flow<Resource<List<GeoSearchItem>>> =
        flow {
            emit(remoteWeather.directGeocode(cityName = cityName))
        }.catch {
            Timber.e(it.message)
        }
}