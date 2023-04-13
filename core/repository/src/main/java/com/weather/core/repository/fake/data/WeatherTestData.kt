package com.weather.core.repository.fake.data

import com.weather.model.Current
import com.weather.model.OneCallCoordinates
import com.weather.model.WeatherData

val listOfWeatherDataTest = listOf(
    WeatherData(
        OneCallCoordinates(
            name = "Tehran",
            lat = 10.0,
            lon = 11.0,
            timezone = "Iran/Tehran",
            timezone_offset = 123456
        ),
        Current(
            clouds = 0,
            dew_point = 0.0,
            dt = 0,
            feels_like = 0.0,
            humidity = 0,
            pressure = 0,
            sunrise = 0,
            sunset = 0,
            temp = 0.0,
            uvi = 0.0,
            visibility = 0,
            wind_deg = 0,
            wind_gust = 0.0,
            wind_speed = 0.0,
            weather = listOf()
        ),
        daily = listOf(),
        hourly = listOf()
    )
)