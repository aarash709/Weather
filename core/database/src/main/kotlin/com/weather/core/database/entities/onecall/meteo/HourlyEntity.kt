package com.weather.core.database.entities.onecall.meteo

import androidx.room.Entity
import androidx.room.ForeignKey
import com.weather.model.Hourly

@Entity(
	tableName = "hourly",
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
data class HourlyEntity(
	val cityName: String,
	val temperature2m: Double,
	val time: String,
	val weatherCode: Int,
	val windSpeed10m: Double,
	val visibility: Double
)

fun HourlyEntity.asDomainModel(): Hourly {
	return Hourly(
		time = time,
//		clouds = TODO(),
//		dew_point = TODO(),
//		dt = 0,
		sunriseSunset = TODO(),
		feelsLike = TODO(),
		humidity = TODO(),
//		pop = TODO(),
		pressure = TODO(),
		temp = temperature2m,
		uvi = TODO(),
		visibility = TODO(),
//		id = TODO(),
//		main = TODO(),
//		description = TODO(),
//		icon = TODO(),
		winDirection = TODO(),
//		wind_gust = TODO(),
		windSpeed = windSpeed10m,
	)
}