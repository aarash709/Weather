package com.experiment.weather.data.database.entities.onecall

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.experiment.weather.data.remote.dto.oncecall.WeatherDto

@Entity(tableName = "one_call_hourly")
data class OneCallHourlyEntity(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val cityName :String,
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pop: Double, //Was Int should be double
    val pressure: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    @Embedded(prefix = "weather_")
    val weather: WeatherDto,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)
