package com.weather.core.network.model.meteoweahter


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoWeather(
    @SerialName("current")
    val current: Current?,
    @SerialName("current_units")
    val currentUnits: CurrentUnits?,
    @SerialName("daily")
    val daily: Daily?,
    @SerialName("daily_units")
    val dailyUnits: DailyUnits?,
    @SerialName("elevation")
    val elevation: Double?,
    @SerialName("generationtime_ms")
    val generationTimeMs: Double?,
    @SerialName("hourly")
    val hourly: Hourly?,
    @SerialName("hourly_units")
    val hourlyUnits: HourlyUnits?,
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
    val longitude: Double?,
    @SerialName("timezone")
    val timezone: String?,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String?,
    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Int?
)