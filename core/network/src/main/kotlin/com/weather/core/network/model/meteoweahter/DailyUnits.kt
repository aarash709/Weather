package com.weather.core.network.model.meteoweahter


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyUnits(
    @SerialName("precipitation_sum")
    val precipitationSum: String?,
    @SerialName("temperature_2m_max")
    val temperature2mMax: String?,
    @SerialName("temperature_2m_min")
    val temperature2mMin: String?,
    @SerialName("time")
    val time: String?,
    @SerialName("weather_code")
    val weatherCode: String?
)