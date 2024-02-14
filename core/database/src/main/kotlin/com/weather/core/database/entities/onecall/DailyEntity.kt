package com.weather.core.database.entities.onecall

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.NO_ACTION
import androidx.room.PrimaryKey
import com.weather.model.Daily

@Entity(
    tableName = "one_call_daily",
    primaryKeys = ["cityName", "dt"],
    foreignKeys = [ForeignKey(
        entity = OneCallEntity::class,
        parentColumns = arrayOf("cityName"),
        childColumns = arrayOf("cityName"),
        onDelete = CASCADE,
        onUpdate = NO_ACTION,
        deferred = true
    )]
)
data class DailyEntity(
    val cityName: String,
    val clouds: Int,
    val dew_point: Double,
    val dt: Long,
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
    val wind_speed: Double,
) {
    fun asDomainModel(): Daily {
        return Daily(
            clouds = clouds,
            dew_point = dew_point,
            dt = dt.toString(),
            humidity = humidity,
            moon_phase = moon_phase,
            moonrise = moonrise,
            moonset = moonset,
            pop = pop,
            pressure = pressure,
            sunrise = sunrise,
            sunset = sunset,
            dayTemp = dayTemp,
            nightTemp = nightTemp,
            uvi = uvi,
            id = id,
            main = main,
            description = description,
            icon = icon,
            wind_deg = wind_deg,
            wind_gust = wind_gust,
            wind_speed = wind_speed
        )
    }
}
