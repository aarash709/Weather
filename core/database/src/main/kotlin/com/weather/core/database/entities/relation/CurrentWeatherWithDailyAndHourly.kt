package com.weather.core.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.core.database.entities.meteo.CurrentEntity
import com.weather.core.database.entities.meteo.DailyEntity
import com.weather.core.database.entities.meteo.HourlyEntity
import com.weather.core.database.entities.meteo.WeatherLocationEntity

data class CurrentWeatherWithDailyAndHourly(
	@Embedded
	val weatherLocation: WeatherLocationEntity,

	@Relation(entity = CurrentEntity::class, parentColumn = "cityName", entityColumn = "cityName")
	val current: CurrentEntity,

	@Relation(entity = DailyEntity::class, parentColumn = "cityName", entityColumn = "cityName")
	val daily: List<DailyEntity>,

	@Relation(entity = HourlyEntity::class, parentColumn = "cityName", entityColumn = "cityName")
	val hourly: List<HourlyEntity>
)