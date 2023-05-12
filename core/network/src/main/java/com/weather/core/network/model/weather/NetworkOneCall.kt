package com.weather.core.network.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class NetworkOneCall(
    val current: NetworkCurrent,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val daily: List<NetworkDaily>,
    val hourly: List<NetworkHourly>,
)