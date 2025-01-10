package com.weather.core.database.entities.onecall.meteo

import androidx.room.Entity
import androidx.room.PrimaryKey

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
