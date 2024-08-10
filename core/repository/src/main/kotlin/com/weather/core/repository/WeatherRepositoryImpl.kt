package com.weather.core.repository

import com.weather.core.database.WeatherLocalDataSource
import com.weather.core.network.WeatherRemoteDatasource
import com.weather.model.Coordinate
import com.weather.model.DailyPreview
import com.weather.model.ManageLocationsData
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteWeather: WeatherRemoteDatasource,
    private val localWeather: WeatherLocalDataSource,
) : WeatherRepository {
    override suspend fun deleteWeatherByCityName(cityNames: List<String>) {
        localWeather.deleteWeatherByCityName(cityNames = cityNames)
    }

    override fun isDatabaseEmpty(): Int = localWeather.databaseIsEmpty()
    override suspend fun getFiveDay(coordinate: Coordinate): List<DailyPreview> {
        return try {
            val data = remoteWeather.getRemoteData(coordinates = coordinate, exclude = "")
            val daily = data.data!!.daily.slice(0..4)
            daily.map {
                DailyPreview(
                    it.temp.day.toInt(),
                    it.temp.night.toInt(),
                    it.dt.toString(),
                    it.weather[0].icon,
                    it.weather[0].description
                )
            }
        } catch (e: IOException) {
            Timber.e("getFiveDay error: ${e.message}")
            listOf()
        }
    }

    override fun getAllForecastWeatherData(): Flow<List<WeatherData>> {
        return localWeather.getAllForecastData()
    }


    override fun getAllWeatherLocations(): Flow<List<ManageLocationsData>> {
        return localWeather.getAllLocalWeatherData().map {
            it.map { data ->
                ManageLocationsData(
                    locationName = data.oneCall.cityName,
                    weatherIcon = data.current.icon,
                    latitude = data.oneCall.lat.toString(),
                    longitude = data.oneCall.lon.toString(),
                    currentTemp = data.current.temp.toString(),
                    humidity = data.current.humidity.toString(),
                    feelsLike = data.current.feels_like.toString()
                )
            }
        }
    }

    override fun getLocalWeatherByCityName(cityName: String): Flow<WeatherData> {
        return localWeather.getLocalWeatherDataByCityName(cityName = cityName)
    }

    @Deprecated("Use syncWeather with Coordinate parameter")
    override suspend fun syncWeather(cityName: String, coordinate: Coordinate) {
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
        //work in progress
        try {
            val remoteWeatherInfo = remoteWeather.getRemoteData(
                coordinates = coordinate,
                exclude = "minutely"
            )
            Timber.e("dt: ${remoteWeatherInfo.data?.daily?.first()?.dt.toString()}")
            Timber.e("timezone: ${remoteWeatherInfo.data?.timezone.toString()}")
            remoteWeatherInfo.let {
                localWeather.insertLocalData(
                    oneCall = remoteWeatherInfo.data!!.toEntity(cityName = coordinate.cityName.toString()),
                    current = remoteWeatherInfo.data!!.current.toEntity(cityName = coordinate.cityName.toString()),
                    currentWeather = remoteWeatherInfo.data!!.current.weather.map {
                        it.toEntity(cityName = coordinate.cityName.toString())
                    },
                    daily = remoteWeatherInfo.data!!.daily.slice(0..4)
                        .map { it.toEntity(cityName = coordinate.cityName.toString()) },
                    hourly = remoteWeatherInfo.data!!.hourly.slice(0..12).map {
                        it.toEntity(cityName = coordinate.cityName.toString())
                    }
                )
                val firstDailyTimeStamp = it.data!!.daily.first().dt
                val firstHourlyTimeStamp = it.data!!.hourly.first().dt
                Timber.e(firstHourlyTimeStamp.toString())
                localWeather.deleteDaily(
                    cityName = coordinate.cityName!!,
                    timeStamp = firstDailyTimeStamp
                )
                localWeather.deleteHourly(
                    cityName = coordinate.cityName!!,
                    timeStamp = firstHourlyTimeStamp
                )
            }
        } catch (e: Exception) {
            Timber.e("sync error: ${e.message}")
        }
    }

    override fun searchLocation(cityName: String): Flow<List<GeoSearchItem>> =
        flow {
            val remoteData = remoteWeather.directGeocode(cityName = cityName)
            if (remoteData.isNotEmpty())
                emit(remoteData)
        }.catch {
            Timber.e("search error: ${it.message}")
        }
}