package com.weather.feature.forecast

import com.weather.model.Current
import com.weather.model.Daily
import com.weather.model.Hourly
import com.weather.model.OneCallCoordinates
import com.weather.model.SettingsData
import com.weather.model.Weather
import com.weather.model.WeatherData

data class SavableForecastData(
    val weather: WeatherData,
    val userSettings: SettingsData,
    val showPlaceHolder: Boolean = true,
) {
    companion object {
        val placeholderDefault = SavableForecastData(
            weather = WeatherData(
                coordinates = OneCallCoordinates(
                    name = "", lat = 0.0, lon = 0.0, timezone = "", timezone_offset = 0

                ), current = Current(
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
                    wind_speed = 0.0,
                    weather = Weather.empty
                ), daily = Daily.empty,
                hourly = Hourly.empty
            ),
            userSettings = SettingsData(windSpeedUnits = null, temperatureUnits = null)
        )
    }
}