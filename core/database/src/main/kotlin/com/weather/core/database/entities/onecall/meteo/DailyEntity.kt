package com.weather.core.database.entities.onecall.meteo

import androidx.room.Entity
import androidx.room.ForeignKey
import com.weather.model.Daily


@Entity(
	tableName = "daily",
	primaryKeys = ["cityName","time"],
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
	val weatherCode: Int,
	val sunrise: String,
	val sunset: String,
	val uvIndex: Double,

)

fun DailyEntity.asDomainModel(): Daily {
	return Daily(
		time = time,
		dayTemp = temperature2mMax,
		nightTemp = temperature2mMin
	)
}