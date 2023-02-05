package com.weather.core.repository

import com.weather.core.network.model.weather.NetworkCurrent
import com.weather.core.network.model.weather.NetworkMinutely
import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.core.network.model.weather.NetworkWeather
import com.weather.entities.onecall.OneCallCurrentEntity
import com.weather.entities.onecall.OneCallEntity
import com.weather.entities.onecall.OneCallMinutelyEntity
import com.weather.entities.onecall.OneCallWeatherEntity

fun NetworkOneCall.toEntity(cityName: String): OneCallEntity {
    return OneCallEntity(
        cityName = cityName,
        lat = lat,
        lon = lon,
        timezone = timezone,
        timezone_offset = timezone_offset
    )
}

fun NetworkMinutely.toMinutelyEntity(cityName: String): OneCallMinutelyEntity {
    return OneCallMinutelyEntity(
        dt = dt,
        precipitation = precipitation,
        cityName = cityName
    )
}

fun NetworkCurrent.toCurrentEntity(cityName: String): OneCallCurrentEntity {
    return OneCallCurrentEntity(
        clouds = clouds,
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
        wind_gust = wind_gust,
        wind_speed = wind_speed,
        cityName = cityName
    )
}

fun NetworkWeather.toEntity(cityName: String): OneCallWeatherEntity {
    return OneCallWeatherEntity(
        cityName = cityName,
        description = description,
        icon = icon,
        id = id,
        main = main
    )
}

fun NetworkCurrent.toEntity(cityName: String): OneCallCurrentEntity {
    return OneCallCurrentEntity(
        cityName = cityName,
        clouds,
        dew_point,
        dt,
        feels_like,
        humidity,
        pressure,
        sunrise,
        sunset,
        temp,
        uvi,
        visibility,
        wind_deg,
        wind_gust,
        wind_speed
    )
}
