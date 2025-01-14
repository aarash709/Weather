package com.weather.core.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.core.database.entities.meteo.CurrentEntity
import com.weather.core.database.entities.meteo.WeatherLocationEntity

data class WeatherAndCurrent(
	@Embedded
	val weatherLocation: WeatherLocationEntity,
	@Relation(parentColumn = "cityName", entityColumn = "cityName")
	val current: CurrentEntity
)
