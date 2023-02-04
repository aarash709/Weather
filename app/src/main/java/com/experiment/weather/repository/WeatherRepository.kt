package com.experiment.weather.repository

import com.experiment.weather.common.utils.Resource
import com.experiment.weather.data.remote.model.ManageLocationsData
import com.experiment.weather.data.remote.model.geocode.GeoSearchItem
import com.experiment.weather.data.remote.model.weatherData.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun searchLocation(cityName: String): Flow<Resource<List<GeoSearchItem>>>

    suspend fun syncLatestWeather(cityName: String, coordinates: Coordinates)

    fun weatherLocalDataStream(cityName: String): Flow<WeatherData>

    fun getAllLocationsWeatherData(): Flow<List<ManageLocationsData>>

    fun getAllForecastWeatherData(): Flow<List<WeatherData>>

    fun databaseIsEmpty(): Int


}