package com.weather.model

import arrow.optics.optics
import kotlin.math.roundToInt

@optics
data class WeatherData(
    val coordinates: OneCallCoordinates,
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
) {
    companion object
}

@optics
data class OneCallCoordinates(
    val name: String,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
) {
    companion object
}

@optics
data class Current(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val currentTemp: Double,
    val uvi: Double,
    val visibility: Int,
    val wind_deg: Int,
    val wind_speed: Double,
    val weather: List<Weather>,
) {
    companion object
}

@optics
data class Daily(
    val clouds: Int,
    val dew_point: Double,
    val dt: String,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val dayTemp: Double,
    val nightTemp: Double,
    val uvi: Double,
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double,
) {
    companion object {
        val empty = List(4) {
            Daily(
                clouds = 0,
                dew_point = 0.0,
                dt = "",
                humidity = 0,
                moon_phase = 0.0,
                moonrise = 0,
                moonset = 0,
                pop = 0.0,
                pressure = 0,
                sunrise = 0,
                sunset = 0,
                dayTemp = 0.0,
                nightTemp = 0.0,
                uvi = 0.0,
                id = 0,
                main = "",
                description = "",
                icon = "",
                wind_deg = 0,
                wind_gust = 0.0,
                wind_speed = 0.0
            )
        }
    }

    fun toDailyPreview(): DailyPreview {
        return DailyPreview(
            tempDay = dayTemp.roundToInt(),
            tempNight = nightTemp.roundToInt(),
            time = dt,
            icon = icon,
            condition = main
        )
    }
}

@optics
data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val time: String,
    val dt: Int,
    val sunriseSunset: String,
    val feels_like: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double,
) {
    companion object {
        val empty = List(5) {
            Hourly(
                clouds = 0,
                dew_point = 0.0,
                time = "",
                dt = 0,
                sunriseSunset = "",
                feels_like = 0.0,
                humidity = 0,
                pop = 0.0,
                pressure = 0,
                temp = 0.0,
                uvi = 0.0,
                visibility = 0,
                id = 0,
                main = "",
                description = "",
                icon = "",
                wind_deg = 0,
                wind_gust = 0.0,
                wind_speed = 0.0
            )
        }
    }
}

@optics
data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double,
) {
    companion object
}

@optics
data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double,
) {
    companion object
}

@optics
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String,
) {
    companion object {
        val empty = listOf(Weather(description = "", icon = "", id = 0, main = ""))
    }
}