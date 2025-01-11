package com.weather.core.repository.fake.data

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
//            clouds = 0,
//            dew_point = 0.0,
//            dt = 0,
			feelsLike = 0.0,
			humidity = 0,
			pressure = 0.0,
			sunrise = 0,
			sunset = 0,
			currentTemp = 0.0,
			uvi = 0.0,
			visibility = 0,
			windDirection = 0,
//            wind_gust = 0.0,
			windSpeed = 12.0,
		),
		daily = listOf(),
		hourly = listOf()
	)
)