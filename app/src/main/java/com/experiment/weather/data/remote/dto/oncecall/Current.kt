package com.experiment.weather.data.remote.dto.oncecall

import com.experiment.weather.data.database.entities.onecall.OneCallCurrentEntity
import kotlinx.serialization.Serializable

@Serializable
data class Current(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val wind_deg: Int,
    val wind_gust: Double?,
    val wind_speed: Double,
    val weather: List<WeatherDto>,
){
    fun asDatabaseModel(cityName:String): OneCallCurrentEntity {
        return OneCallCurrentEntity(
            clouds = clouds,
            dew_point = dew_point,
            dt = dt,
            feels_like = feels_like,
            humidity = humidity,
            pressure = pressure,
            sunrise = sunrise,
            sunset = sunset,
            temp = temp,
            uvi = uvi,
            visibility = visibility,
            wind_deg = wind_deg,
            wind_gust = wind_gust,
            wind_speed = wind_speed,
            cityName = cityName
        )
    }
}