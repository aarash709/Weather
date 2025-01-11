package com.weather.core.testing.data

import com.weather.core.database.entities.onecall.CurrentWeatherEntity
import com.weather.core.database.entities.onecall.OneCallEntity
import com.weather.core.database.entities.onecall.meteo.HourlyEntity

val oneCall = OneCallEntity(
	cityName = "Sommersdale",
	orderIndex = 0,
	lat = 4.5,
	lon = 6.7,
	timezone = "graecis",
	timezone_offset = 5109

)
val current = com.weather.core.database.entities.onecall.meteo.CurrentEntity(
	cityName = "Elk Grove",
	apparentTemperature = 12.13,
	isDay = 5917,
	precipitation = 14.15,
	pressureMsl = 16.17,
	relativeHumidity2m = 5494,
	surfacePressure = 18.19,
	temperature2m = 20.21,
	time = "ne",
	weatherCode = 4140,
	windDirection10m = 8474,
	windSpeed10m = 22.23
)
val currentWeather = listOf(
	CurrentWeatherEntity(
		cityName = "Sommersdale",
		description = "eius",
		icon = "docendi",
		id = 3682,
		main = "deseruisse"

	)
)
val daily = listOf(
	com.weather.core.database.entities.onecall.meteo.DailyEntity(
		cityName = "Oakston",
		precipitationSum = 32.33,
		temperature2mMax = 34.35,
		temperature2mMin = 36.37,
		time = "intellegebat",
		weatherCode = 1836,
		sunrise = "id",
		sunset = "doctus",
		uvIndex = 38.39
	)
)
val hourly = listOf(
	HourlyEntity(
		cityName = "Avalon",
		temperature2m = 46.47,
		time = "laoreet",
		weatherCode = 1157,
		windSpeed10m = 48.49,
		visibility = 50.51
	)
)