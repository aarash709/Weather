package com.weather.core.database.entities.onecall

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.weather.model.Current
import com.weather.model.Weather

@Entity(
    tableName = "one_call_current", foreignKeys = [ForeignKey(
        entity = OneCallEntity::class,
        parentColumns = arrayOf("cityName"),
        childColumns = arrayOf("cityName"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class CurrentEntity(
    @PrimaryKey(autoGenerate = false)
    val cityName: String,
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
//    /**
//     * parent Id
//     */
//    val oneCallCityName: String? = null,
//    @PrimaryKey
//    val currentId: Int? = null
) {
    fun asDomainModel(weather: List<Weather>): Current {
        return Current(
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
            weather = weather,
        )
    }
}
