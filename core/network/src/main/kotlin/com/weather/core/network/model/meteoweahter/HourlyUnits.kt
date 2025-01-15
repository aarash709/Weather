package com.weather.core.network.model.meteoweahter


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyUnits(
    @SerialName("temperature_2m")
    val temperature2m: String?,
    @SerialName("time")
    val time: String?,
    @SerialName("weather_code")
    val weatherCode: String?,
    @SerialName("wind_speed_10m")
    val windSpeed10m: String?,
    @SerialName("is_day")
    val isDay: String?
)