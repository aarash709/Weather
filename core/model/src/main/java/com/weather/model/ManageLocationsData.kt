package com.weather.model

data class ManageLocationsData(
    val locationName: String,
    val latitude: String,
    val longitude: String,
    val currentTemp: String,
    val humidity: String,
    val feelsLike: String,
    val isFavorite: Boolean = false,
)
