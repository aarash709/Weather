package com.weather.core.repository

import com.weather.core.database.entities.onecall.*
import com.weather.core.network.model.weather.*
import com.weather.model.FeelsLike

fun NetworkOneCall.toEntity(cityName: String): OneCallEntity {
    return OneCallEntity(
        cityName = cityName,
        lat = lat,
        orderIndex = 0,
        lon = lon,
        timezone = timezone,
        timezone_offset = timezone_offset,
    )
}

fun NetworkWeather.toEntity(cityName: String): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        cityName = cityName,
        description = description,
        icon = icon,
        id = id,
        main = main
    )
}

fun NetworkCurrent.toEntity(cityName: String): CurrentEntity {
    return CurrentEntity(
        cityName = cityName,
        clouds = clouds,
        icon = weather.first().icon,
        dew_point = dew_point,
        dt = dt,
        feels_like = feels_like,
        humidity = humidity,
        pressure = pressure,
        sunrise = sunrise,
        sunset = sunset,
        temp = temp,
        uvi = uvi,
        visibility = visibility,
        wind_deg = wind_deg,
        wind_speed = wind_speed
    )
}

fun NetworkDaily.toEntity(cityName: String): DailyEntity {
    return DailyEntity(
        cityName = cityName,
        clouds = clouds,
        dew_point = dew_point,
        dt = dt,
        humidity = humidity,
        moon_phase = moon_phase,
        moonrise = moonrise,
        moonset = moonset,
        pop = pop,
        pressure = pressure,
        sunrise = sunrise,
        sunset = sunset,
        dayTemp = temp.max,
        nightTemp = temp.min,
        id = weather.first().id,
        main = weather.first().main,
        description = weather.first().description,
        icon = weather.first().icon,
        uvi = uvi,
        wind_deg = wind_deg,
        wind_gust = wind_gust,
        wind_speed = wind_speed
    )
}

fun NetworkFeelsLike.toFeelsLike(): FeelsLike {
    return FeelsLike(
        day,
        eve,
        morn = morn,
        night = night
    )
}

fun NetworkHourly.toEntity(cityName: String): OneCallHourlyEntity {
    return OneCallHourlyEntity(
        cityName = cityName,
        clouds = clouds,
        dew_point = dew_point,
        dt = dt,
        feels_like = feels_like,
        humidity = humidity,
        pop = pop,
        pressure = pressure,
        temp = temp,
        uvi = uvi,
        visibility = visibility,
        id = weather.first().id,
        main = weather.first().main,
        description = weather.first().description,
        icon = weather.first().icon,
        wind_deg = wind_deg,
        wind_gust = wind_gust,
        wind_speed = wind_speed
    )
}