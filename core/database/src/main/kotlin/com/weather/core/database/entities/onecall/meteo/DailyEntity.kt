package com.weather.core.database.entities.onecall.meteo

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
	tableName = "daily",
	foreignKeys = [ForeignKey(
		entity = WeatherLocationEntity::class,
		parentColumns = arrayOf("cityName"),
		childColumns = arrayOf("cityName"),
		onDelete = ForeignKey.CASCADE,
		onUpdate = ForeignKey.NO_ACTION,
		deferred = true
	)]
)
data class DailyEntity(
	val cityName: String,
	val precipitationSum: Double,
	val temperature2mMax: Double,
	val temperature2mMin: Double,
	val time: String,
	val weatherCode: Int

)
