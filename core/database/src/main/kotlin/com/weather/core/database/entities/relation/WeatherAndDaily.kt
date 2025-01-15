package com.weather.core.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.core.database.entities.meteo.DailyEntity
import com.weather.core.database.entities.meteo.WeatherLocationEntity

data class WeatherAndDaily(
	@Embedded
	val weatherLocation: WeatherLocationEntity,
	@Relation(parentColumn = "cityName", entityColumn = "cityName")
	val daily: DailyEntity

)
