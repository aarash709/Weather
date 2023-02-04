package com.experiment.weather.data.remote.dto.oncecall

import kotlinx.serialization.Serializable

@Serializable
data class TempDto(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)