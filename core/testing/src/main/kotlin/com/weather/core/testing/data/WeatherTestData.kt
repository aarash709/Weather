package com.weather.core.testing.data

import com.weather.core.database.entities.onecall.CurrentEntity
import com.weather.core.database.entities.onecall.CurrentWeatherEntity
import com.weather.core.database.entities.onecall.DailyEntity
import com.weather.core.database.entities.onecall.OneCallEntity
import com.weather.core.database.entities.onecall.OneCallHourlyEntity

val oneCall = OneCallEntity(
    cityName = "Sommersdale",
    orderIndex = 0,
    lat = 4.5,
    lon = 6.7,
    timezone = "graecis",
    timezone_offset = 5109

)
val current = CurrentEntity(
    cityName = "Sommersdale",
    icon = "icon",
    clouds = 8337,
    dew_point = 48.49,
    dt = 8025,
    feels_like = 50.51,
    humidity = 4375,
    pressure = 9379,
    sunrise = 3962,
    sunset = 8896,
    temp = 52.53,
    uvi = 54.55,
    visibility = 7734,
    wind_deg = 2563,
    wind_speed = 56.57

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
    DailyEntity(
        cityName = "Sommersdale",
        clouds = 5294,
        dew_point = 74.75,
        time = 8669,
        humidity = 3322,
        moon_phase = 76.77,
        moonrise = 4691,
        moonset = 6711,
        pop = 78.79,
        pressure = 4394,
        sunrise = 6929,
        sunset = 7243,
        dayTemp = 80.81,
        nightTemp = 82.83,
        id = 4976,
        main = "vivamus",
        description = "nibh",
        icon = "vestibulum",
        uvi = 84.85,
        wind_deg = 7494,
        wind_gust = 86.87,
        wind_speed = 88.89

    )
)
val hourly = listOf(
    OneCallHourlyEntity(
        cityName = "Sommersdale",
        clouds = 7050,
        dew_point = 104.105,
        dt = 9104,
        feels_like = 106.107,
        humidity = 6741,
        pop = 108.109,
        pressure = 7900,
        temp = 110.111,
        uvi = 112.113,
        visibility = 6223,
        id = 8661,
        main = "ponderum",
        description = "deterruisset",
        icon = "nostra",
        wind_deg = 1451,
        wind_gust = 114.115,
        wind_speed = 116.117
    )
)