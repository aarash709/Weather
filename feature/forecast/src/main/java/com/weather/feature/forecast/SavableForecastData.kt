package com.weather.feature.forecast

import com.weather.model.SettingsData
import com.weather.model.WeatherData

data class SavableForecastData(
    val weather: WeatherData,
    val userSettings: SettingsData,
    val showPlaceHolder: Boolean = true,
)