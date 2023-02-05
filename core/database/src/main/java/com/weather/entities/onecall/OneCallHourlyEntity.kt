package com.weather.entities.onecall

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.model.Weather

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
    val weather: Weather,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)
