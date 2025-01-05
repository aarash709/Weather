package com.weather.core.repository

import com.weather.model.Coordinate
import com.weather.model.DailyPreview
import com.weather.model.ManageLocationsData
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

	fun searchLocation(cityName: String): Flow<List<GeoSearchItem>>

	/**
	 * @param params are values to include in the json response for example:
	 * "current=temperature_2m,relative_humidity_2m..."
	 * each value should be separated with a ","
	 * visit "https://open-meteo.com/en/docs" for more
	 */
	suspend fun getCurrent(latitude: String, longitude: String, params: String)

	/**
	 * @param params are values to include in the json response for example:
	 * "daily=weather_code,temperature_2m_max..."
	 * each value should be separated with a ","
	 * visit "https://open-meteo.com/en/docs" for more
	 */
	suspend fun getDaily(latitude: String, longitude: String, params: String)

	/**
	 * @param params are values to include in the json response for example:
	 * "hourly=temperature_2m,weather_code,wind_speed_10m..."
	 * each value should be separated with a ","
	 * visit "https://open-meteo.com/en/docs" for more
	 */
	suspend fun getHourly(latitude: String, longitude: String, params: String)

	suspend fun syncWeather(cityName: String, coordinate: Coordinate)

	suspend fun syncWeather(coordinate: Coordinate)

	suspend fun deleteWeatherByCityName(cityNames: List<String>)

//	suspend fun getFiveDay(coordinate: Coordinate): List<DailyPreview>

	fun getLocalWeatherByCityName(cityName: String): Flow<WeatherData>

	fun getAllWeatherLocations(): Flow<List<ManageLocationsData>>

	suspend fun reorderData(locations: List<ManageLocationsData>)

	fun getAllForecastWeatherData(): Flow<List<WeatherData>>

	fun isDatabaseEmpty(): Boolean

}