package com.weather.core.database.entities.onecall.meteo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
	tableName = "current",
	foreignKeys = [ForeignKey(
		entity = WeatherLocationEntity::class,
		parentColumns = arrayOf("cityName"),
		childColumns = arrayOf("cityName"),
		onDelete = ForeignKey.CASCADE,
		onUpdate = ForeignKey.NO_ACTION,
		deferred = true
	)]
)
data class CurrentEntity(
	@PrimaryKey(autoGenerate = false)
	val cityName: String,
	val apparentTemperature: Double,
	val isDay: Int,
	val precipitation: Double,
	val pressureMsl: Double,
	val relativeHumidity2m: Int,
	val surfacePressure: Double,
	val temperature2m: Double,
	val time: String,
	val weatherCode: Int,
	val windDirection10m: Int,
	val windSpeed10m: Double
)
