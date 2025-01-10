package com.weather.core.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.core.database.entities.onecall.meteo.CurrentEntity
import com.weather.core.database.entities.onecall.meteo.DailyEntity
import com.weather.core.database.entities.onecall.meteo.HourlyEntity
import com.weather.core.database.entities.onecall.meteo.WeatherLocationEntity

data class CurrentWeatherWithDailyAndHourly(
	@Embedded
	val weatherLocation: WeatherLocationEntity,

	@Relation(entity = CurrentEntity::class, parentColumn = "cityName", entityColumn = "cityName")
	val current: WeatherAndCurrent,

	@Relation(entity = DailyEntity::class, parentColumn = "cityName", entityColumn = "cityName")
	val daily: List<WeatherAndDaily>,

	@Relation(entity = HourlyEntity::class, parentColumn = "cityName", entityColumn = "cityName")
	val hourly: List<WeatherAndHourly>
)