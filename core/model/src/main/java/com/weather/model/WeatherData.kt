package com.weather.model

import arrow.optics.optics
import kotlin.math.roundToInt

@optics
data class WeatherData(
    val coordinates: WeatherCoordinates,
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
) {
    companion object
}

@optics
data class WeatherCoordinates(
    val name: String,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Int,
) {
    companion object
}

@optics
data class Current(
    val time: String,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Double,
    val sunrise: Int,
    val sunset: Int,
    val currentTemp: Double,
    val uvi: Double,
    val visibility: Int,
    val windDirection: Int,
    val windSpeed: Double,
) {
    companion object
}

@optics
data class Daily(
    val time: String,
    val dayTemp: Double,
    val nightTemp: Double,
) {
    companion object {
        val empty = List(4) {
            Daily(
                time = "",
                dayTemp = 0.0,
                nightTemp = 0.0,

            )
        }
    }

    fun toDailyPreview(): DailyPreview {
        return DailyPreview(
            tempDay = dayTemp.roundToInt(),
            tempNight = nightTemp.roundToInt(),
            time = time,
        )
    }
}

@optics
data class Hourly(
    val time: String,
    val sunriseSunset: String,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val winDirection: Int,
    val windSpeed: Double,
) {
    companion object {
        val empty = List(5) {
            Hourly(
                time = "",
                sunriseSunset = "",
                humidity = 0,
                pressure = 0,
                temp = 0.0,
                uvi = 0.0,
                visibility = 0,
                winDirection = 0,
                windSpeed = 0.0
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