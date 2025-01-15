package com.weather.core.repository.data

import com.weather.model.Current
import com.weather.model.WeatherCoordinates
import com.weather.model.WeatherData

val listOfWeatherDataTest = listOf(
	WeatherData(
		WeatherCoordinates(
			name = "Tehran",
			lat = 10.0,
			lon = 11.0,
			timezone = "Iran/Tehran",
			timezoneOffset = 123456
		),
		Current(
			time = "",
			feelsLike = 0.0,
			humidity = 0,
			pressure = 0.0,
			sunrise = 10,
			sunset = 40,
			currentTemp = 0.0,
			uvi = 0.0,
			visibility = 0,
			windDirection = 0,
			windSpeed = 12.0,
			condition = "Rain"
		),
		daily = listOf(),
		hourly = listOf()
	)
)