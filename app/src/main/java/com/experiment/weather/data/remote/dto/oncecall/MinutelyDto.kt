package com.experiment.weather.data.remote.dto.oncecall

import com.experiment.weather.data.database.entities.onecall.OneCallMinutelyEntity
import kotlinx.serialization.Serializable

@Serializable
data class MinutelyDto(
    val dt: Int,
    val precipitation: Int,
) {
    fun asDatabaseModel(cityName: String): OneCallMinutelyEntity {
        return OneCallMinutelyEntity(
            cityName = cityName,
            dt = dt,
            precipitation = precipitation
        )
    }
}