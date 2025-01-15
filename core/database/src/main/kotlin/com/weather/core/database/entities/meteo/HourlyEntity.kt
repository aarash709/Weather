package com.weather.core.database.entities.meteo

import androidx.room.Entity
import androidx.room.ForeignKey
import com.weather.model.Hourly

@Entity(
	tableName = "hourly",
	primaryKeys = ["cityName", "time"],
	foreignKeys = [ForeignKey(
		entity = WeatherLocationEntity::class,
		parentColumns = arrayOf("cityName"),
		childColumns = arrayOf("cityName"),
		onDelete = ForeignKey.CASCADE,
		onUpdate = ForeignKey.NO_ACTION,
		deferred = true
	)]
)
data class HourlyEntity(
	val cityName: String,
	val temperature2m: Double,
	val time: String,
	val weatherCode: Int,
	val windSpeed10m: Double,
	val visibility: Double,
	val isDay: Int,
)

fun HourlyEntity.asDomainModel(
	humidity: Int,
	pressure: Int,
	uvi: Double,
	windDirection: Int,
	iconUrl: String
): Hourly {
	return Hourly(
		time = time,
		isDay = isDay,
		sunriseSunset = "",
		humidity = humidity,
		pressure = pressure,
		temp = temperature2m,
		uvi = uvi,
		visibility = visibility.toInt(),
		winDirection = windDirection,
		windSpeed = windSpeed10m,
		weatherCode = weatherCode,
		iconUrl = iconUrl
	)
}