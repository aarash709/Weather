package com.weather.model

data class SettingsData(
    val windSpeedUnits: WindSpeedUnits = WindSpeedUnits.KM,
    val temperatureUnits: TemperatureUnits = TemperatureUnits.C,
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
