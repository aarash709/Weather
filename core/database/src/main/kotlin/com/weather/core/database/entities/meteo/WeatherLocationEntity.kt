package com.weather.core.database.entities.meteo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.model.WeatherCoordinates

@Entity(
	tableName = "weather_location"
)
data class WeatherLocationEntity(
	@PrimaryKey(autoGenerate = false)
	val cityName: String,
	val orderIndex: Int? = null,
	val lat: Double,
	val lon: Double,
	val timezone: String,
	val timezoneOffset: Int,
)

fun WeatherLocationEntity.toCoordinate(cityName: String): WeatherCoordinates {
	return WeatherCoordinates(
		name = cityName,
		lat = lat,
		lon = lon,
		timezone = timezone,
		timezoneOffset = timezoneOffset
	)
}