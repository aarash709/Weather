package com.example.entities.onecall

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.FeelsLike
import com.example.model.Temp
import com.example.model.Weather

@Entity(tableName = "one_call_daily")
data class OneCallDailyEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val cityName : String="",
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    @Embedded(prefix = "feels_")
    val feels_like: FeelsLike,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double, //was Int
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    @Embedded(prefix = "temp_")
    val temp: Temp,
    @Embedded(prefix ="weather_")
    val weather: Weather,
    val uvi: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)
