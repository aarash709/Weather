package com.weather.model

import arrow.optics.optics

@optics
data class ManageLocationsData(
    val locationName: String,
    val weatherIcon:String,
    val latitude: String,
    val longitude: String,
    val currentTemp: String,
    val humidity: String,
    val feelsLike: String,
    val isFavorite: Boolean = false,
){
    companion object
}
