package com.weather.entities.onecall

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.model.Daily

@Entity(tableName = "one_call_daily")
data class DailyEntity(
    @PrimaryKey(autoGenerate = false)
    val cityName : String="",
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
//    @Embedded(prefix = "feels_")
//    val feels_like: FeelsLike,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double, //was Int
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val dayTemp: Double,
    val nightTemp: Double,
//    @Embedded(prefix ="weather_")
//    val weather: Weather,
    @ColumnInfo(name = "weather_id")
    val id: Int,
    @ColumnInfo(name = "weather_main")
    val main: String,
    @ColumnInfo(name = "weather_desc")
    val description: String,
    @ColumnInfo(name = "weather_icon")
    val icon: String,
    val uvi: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
){
    fun asDomainModel(): Daily{
        return Daily(
            clouds,
            dew_point,
            dt,
            humidity,
            moon_phase,
            moonrise,
            moonset,
            pop,
            pressure,
            sunrise,
            sunset,
            dayTemp,
            nightTemp,
            uvi,
            id,
            main,
            description,
            icon,
            wind_deg,
            wind_gust,
            wind_speed
        )
    }
}
