package com.weather.core.repository

import com.example.model.Coordinates
import com.example.model.ManageLocationsData
import com.example.model.Resource
import com.example.model.WeatherData
import com.example.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun searchLocation(cityName: String): Flow<Resource<List<GeoSearchItem>>>

    suspend fun syncLatestWeather(cityName: String, coordinates: Coordinates)

    fun weatherLocalDataStream(cityName: String): Flow<WeatherData>

    fun getAllLocationsWeatherData(): Flow<List<ManageLocationsData>>

    fun getAllForecastWeatherData(): Flow<List<WeatherData>>

    fun databaseIsEmpty(): Int


}