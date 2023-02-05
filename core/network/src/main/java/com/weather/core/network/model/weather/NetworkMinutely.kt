package com.weather.core.network.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class NetworkMinutely(
    val dt: Int,
    val precipitation: Int,
) {
//    fun asDatabaseModel(cityName: String): OneCallMinutelyEntity {
//        return OneCallMinutelyEntity(
//            cityName = cityName,
//            dt = dt,
//            precipitation = precipitation
//        )
//    }
}