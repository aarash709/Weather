package com.weather.core.network.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class NetworkDaily(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: NetworkFeelsLike,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double, //was Int
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: TempDto,
    val uvi: Double,
    val weather: List<NetworkWeather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
){
//    fun asDatabaseModel(cityName: String): List<OneCallDaily> {
//        return listOf(OneCallDaily(
//            0,
//            cityName,
//            clouds,
//            dew_point,
//            dt,
//            feels_like,
//            humidity,
//            moon_phase,
//            moonrise,
//            moonset,
//            pop,
//            pressure,
//            sunrise,
//            sunset,
//            temp,
//            weather.first(),
//            uvi,
//            wind_deg,
//            wind_gust,
//            wind_speed
//        ))
//    }
}