package com.weather.core.database.entities.onecall.meteo

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
	tableName = "hourly",
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
	val windSpeed10m: Double
)
