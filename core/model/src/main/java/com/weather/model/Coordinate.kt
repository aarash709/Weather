package com.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    val cityName: String? = null,
    val latitude: String,
    val longitude: String,
)