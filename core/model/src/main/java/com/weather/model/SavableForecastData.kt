package com.weather.model

data class SavableForecastData(
    val weather: WeatherData,
    val showPlaceHolder: Boolean = true,
) {
    companion object {
        val placeholderDefault = SavableForecastData(
            weather = WeatherData(
                coordinates = WeatherCoordinates(
                    name = "", lat = 0.0, lon = 0.0, timezone = "", timezoneOffset = 0

                ), current = Current(
                    time = "",
                    feelsLike = 0.0,
                    humidity = 0,
                    pressure = 0.0,
                    sunrise = 0,
                    sunset = 0,
                    currentTemp = 0.0,
                    uvi = 0.0,
                    visibility = 0,
                    windDirection = 0,
                    windSpeed = 0.0,
                ), daily = Daily.empty,
                hourly = Hourly.empty
            ),
        )
    }
}