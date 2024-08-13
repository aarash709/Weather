package com.weather.model

data class SavableForecastData(
    val weather: WeatherData,
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
                    currentTemp = 0.0,
                    uvi = 0.0,
                    visibility = 0,
                    wind_deg = 0,
                    wind_speed = 0.0,
                    weather = Weather.empty
                ), daily = Daily.empty,
                hourly = Hourly.empty
            ),
        )
    }
}