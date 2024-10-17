package com.weather.model

import arrow.optics.optics

@optics
data class ManageLocationsData(
    val locationName: String,
    val weatherIcon:String,
    val latitude: String,
    val longitude: String,
    val timezone: String,
    val timezoneOffset: Int,
    val currentTemp: String,
    val humidity: String,
    val feelsLike: String,
    val isFavorite: Boolean = false,
    val listOrder:Int
){
    companion object
}
