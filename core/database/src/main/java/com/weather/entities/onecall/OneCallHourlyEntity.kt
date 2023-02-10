package com.weather.entities.onecall

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.weather.model.Hourly

@Entity(
    tableName = "one_call_hourly", foreignKeys = [ForeignKey(
        entity = OneCallEntity::class,
        parentColumns = arrayOf("cityName"),
        childColumns = arrayOf("cityName"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class OneCallHourlyEntity(
    val cityName: String,
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
//    @Embedded(prefix = "weather_")
//    val weather: Weather,
    @ColumnInfo(name = "weather_id")
    val id: Int,
    @ColumnInfo(name = "weather_main")
    val main: String,
    @ColumnInfo(name = "weather_desc")
    val description: String,
    @ColumnInfo(name = "weather_icon")
    val icon: String,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double,
    @PrimaryKey(autoGenerate = true)
    val hourlyId: Int = 0,
) {
    fun asDomainModel(): Hourly {
        return Hourly(
            clouds = clouds,
            dew_point = dew_point,
            dt = dt.toString(),
            feels_like = feels_like,
            humidity = humidity,
            pop = pop,
            pressure = pressure,
            temp = temp,
            uvi = uvi,
            visibility = visibility,
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
