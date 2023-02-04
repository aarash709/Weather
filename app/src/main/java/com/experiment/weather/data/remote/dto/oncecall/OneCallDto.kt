package com.experiment.weather.data.remote.dto.oncecall

import com.experiment.weather.data.database.entities.onecall.OneCallCurrentEntity
import com.experiment.weather.data.database.entities.onecall.OneCallEntity
import com.experiment.weather.data.database.entities.onecall.OneCallMinutelyEntity
import kotlinx.serialization.Serializable

@Serializable
data class OneCallDto(
    val current: Current,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
//    val minutely: List<MinutelyDto>,
    val daily: List<DailyDto>,
    val hourly: List<HourlyDto>,
) {
    fun toOneCallEntity(cityName: String): OneCallEntity {
        return OneCallEntity(
            cityName = cityName,
            lat = lat,
            lon = lon,
            timezone = timezone,
            timezone_offset = timezone_offset
        )
    }
    fun MinutelyDto.toMinutelyEntity(cityName: String): OneCallMinutelyEntity{
        return OneCallMinutelyEntity(
            dt = dt,
            precipitation = precipitation,
            cityName = cityName
        )
    }

    fun Current.toCurrentEntity(cityName : String): OneCallCurrentEntity{
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
