package com.weather.core.network.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class NetworkDaily(
    val clouds: Int,
    val dew_point: Double,
    val dt: Long,
    val feels_like: NetworkFeelsLike,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: TempDto,
    val uvi: Double,
    val weather: List<NetworkWeather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)