package com.experiment.weather.data.remote.model

data class ManageLocationsData(
    val locationName: String,
    val currentTemp: String,
    val humidity: String,
    val feelsLike: String,
//    val tempHigh: Int,
//    val tempLow:Int
)
