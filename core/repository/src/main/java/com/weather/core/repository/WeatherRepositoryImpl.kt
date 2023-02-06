package com.weather.core.repository

import com.weather.WeatherLocalDataSource
import com.weather.core.network.WeatherRemoteDatasource
import com.weather.model.Coordinates
import com.weather.model.ManageLocationsData
import com.weather.model.Resource
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

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
        remoteWeatherInfo.let {
            localWeather.insertLocalData(
                oneCall = remoteWeatherInfo.data!!.toEntity(cityName = cityName),
                current = remoteWeatherInfo.data!!.current.toEntity(cityName = cityName),
                currentWeather = remoteWeatherInfo.data!!.current.weather.map {
                    it.toEntity(cityName= cityName)
                }
            )
        }
    }

    override suspend fun searchLocation(cityName: String): Flow<Resource<List<GeoSearchItem>>> =
        flow {
            emit(remoteWeather.directGeocode(cityName = cityName))
        }.catch {
            Timber.e(it.message)
        }
}