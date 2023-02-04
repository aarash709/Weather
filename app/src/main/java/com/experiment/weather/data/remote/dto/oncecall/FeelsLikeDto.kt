package com.experiment.weather.data.remote.dto.oncecall

import kotlinx.serialization.Serializable

@Serializable
data class FeelsLikeDto(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)