package com.weather.core.repository

import com.weather.core.database.entities.onecall.meteo.CurrentEntity
import com.weather.core.database.entities.onecall.meteo.DailyEntity
import com.weather.core.database.entities.onecall.meteo.HourlyEntity
import com.weather.core.database.entities.onecall.meteo.WeatherLocationEntity
import com.weather.core.network.model.meteoweahter.Daily
import com.weather.core.network.model.meteoweahter.Hourly
import com.weather.core.network.model.meteoweahter.NetworkCurrent

fun NetworkCurrent.toLocationEntity(
	cityName: String,
	latitude: Double,
	longitude: Double,
): WeatherLocationEntity {
	return WeatherLocationEntity(
		cityName = cityName,
		lat = latitude,
		lon = longitude,
		timezone = timezone,
		timezoneOffset = utcOffsetSeconds
	)
}

fun NetworkCurrent.toEntity(cityName: String): CurrentEntity {
	return CurrentEntity(
		cityName = cityName,
		apparentTemperature = current.apparentTemperature,
		isDay = current.isDay,
		precipitation = current.precipitation,
		pressureMsl = current.pressureMsl,
		relativeHumidity2m = current.relativeHumidity2m,
		surfacePressure = current.surfacePressure,
		temperature2m = current.temperature2m,
		time = current.time,
		weatherCode = current.weatherCode,
		windDirection10m = current.windDirection10m,
		windSpeed10m = current.windSpeed10m
	)
}

fun Daily.toEntity(cityName: String): List<DailyEntity> {
	return time.mapIndexed { i, time ->
		DailyEntity(
			cityName = cityName,
			precipitationSum = precipitationSum[i],
			temperature2mMax = temperature2mMax[i],
			temperature2mMin = temperature2mMin[i],
			time = time,
			weatherCode = weatherCode[i],
			sunrise = sunrise[i],
			sunset = sunset[i],
			uvIndex = uvIndex[i]
		)
	}
}

fun Hourly.toEntity(cityName: String,): List<HourlyEntity> {
	return time.mapIndexed { i, time ->
		HourlyEntity(
			cityName = cityName,
			temperature2m = temperature2m[i],
			time = time,
			weatherCode = weatherCode[i],
			windSpeed10m = windSpeed10m[i],
			visibility = visibility[i]
		)
	}
}