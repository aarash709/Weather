package com.weather.model

data class WeatherPreview(
    val cityName: String,
    val countryName: String,
    val daily: DailyPreview,

)
data class DailyPreview(
    val temp: String,
    val time: String,
    val icon: String,
)