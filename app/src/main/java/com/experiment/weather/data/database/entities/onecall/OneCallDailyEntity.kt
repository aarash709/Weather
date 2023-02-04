package com.experiment.weather.data.database.entities.onecall

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.experiment.weather.data.remote.dto.oncecall.FeelsLikeDto
import com.experiment.weather.data.remote.dto.oncecall.TempDto
import com.experiment.weather.data.remote.dto.oncecall.WeatherDto

@Entity(tableName = "one_call_daily")
data class OneCallDailyEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val cityName : String="",
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    @Embedded(prefix = "feels_")
    val feels_like: FeelsLikeDto,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double, //was Int
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    @Embedded(prefix = "temp_")
    val temp: TempDto,
    @Embedded(prefix ="weather_")
    val weather: WeatherDto,
    val uvi: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)
