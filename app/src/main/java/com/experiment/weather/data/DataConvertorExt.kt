package com.experiment.weather.data

import com.experiment.weather.data.database.entities.onecall.OneCallDailyEntity
import com.experiment.weather.data.database.entities.onecall.OneCallEntity
import com.experiment.weather.data.database.entities.onecall.OneCallHourlyEntity
import com.experiment.weather.data.remote.dto.oncecall.DailyDto
import com.experiment.weather.data.remote.dto.oncecall.HourlyDto
import com.experiment.weather.data.remote.dto.oncecall.OneCallDto


/*fun CurrentWeatherDto.asDatabaseModel(): WeatherDataEntity {
    return WeatherDataEntity(
        base,
        clouds,
        cod,
        coord,
        dt,
        id,
        main,
        name,
        sys,
        timezone,
        visibility,
        weather.last(),
        wind
    )
}*/

fun OneCallDto.asDatabaseModel(cityName: String): OneCallEntity {
    return OneCallEntity(
        cityName,
        lat,
        lon,
        timezone,
        timezone_offset
    )
}

fun List<DailyDto>.asDatabaseModel(cityName: String): List<OneCallDailyEntity> {
    val list = mutableListOf<OneCallDailyEntity>()
    this.forEach { daily ->
        daily.apply {
            list += (listOf(
                OneCallDailyEntity(
                    0,
                    cityName,
                    clouds,
                    dew_point,
                    dt,
                    feels_like,
                    humidity,
                    moon_phase,
                    moonrise,
                    moonset,
                    pop,
                    pressure,
                    sunrise,
                    sunset,
                    temp,
                    weather.first(),
                    uvi,
                    wind_deg,
                    wind_gust,
                    wind_speed
                )
            )
                    )
        }
    }
    return list
}

@JvmName(name = "get_hourly_list")
fun List<HourlyDto>.asDatabaseModel(cityName: String): List<OneCallHourlyEntity> {
    val list = mutableListOf<OneCallHourlyEntity>()
    this.forEach { hourly ->
        hourly.apply {
            list += (listOf(
                OneCallHourlyEntity(
                    0,
                    cityName,
                    clouds,
                    dew_point,
                    dt,
                    feels_like,
                    humidity,
                    pop,
                    pressure,
                    temp,
                    uvi,
                    visibility,
                    weather.first(),
                    wind_deg,
                    wind_gust,
                    wind_speed
                )
            )
                    )
        }
    }
    return list
}




