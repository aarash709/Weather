package com.weather.core.network.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class NetworkWeather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)
