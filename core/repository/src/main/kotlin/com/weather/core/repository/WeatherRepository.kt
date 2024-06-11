package com.weather.core.repository

import com.weather.model.Coordinate
import com.weather.model.DailyPreview
import com.weather.model.ManageLocationsData
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun searchLocation(cityName: String): Flow<List<GeoSearchItem>>

    suspend fun syncWeather(cityName: String, coordinate: Coordinate)

    suspend fun syncWeather(coordinate: Coordinate)

    suspend fun deleteWeatherByCityName(cityNames: List<String>)

    suspend fun getFiveDay(coordinate: Coordinate) : List<DailyPreview>

    fun getLocalWeatherByCityName(cityName: String): Flow<WeatherData>

    fun getAllWeatherLocations(): Flow<List<ManageLocationsData>>

    fun getAllForecastWeatherData(): Flow<List<WeatherData>>

    fun isDatabaseEmpty(): Int

}