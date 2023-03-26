package com.weather.model

data class SettingsData(
    val windSpeedUnits: WindSpeedUnits,
    val temperatureUnits: TemperatureUnits,
)

enum class WindSpeedUnits {
    KM,
    MS,
    MPH,
}

enum class TemperatureUnits {
    C,
    F,
}
