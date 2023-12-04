package com.weather.core.network.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class NetworkFeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)