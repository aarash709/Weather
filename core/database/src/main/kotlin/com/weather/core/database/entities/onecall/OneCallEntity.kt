package com.weather.core.database.entities.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.model.WeatherCoordinates

@Entity(tableName = "one_call")
data class OneCallEntity(
    @PrimaryKey(autoGenerate = false)
    val cityName: String,
    val orderIndex: Int? = null,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
) {
    fun asDomainModel(): WeatherCoordinates {
        return WeatherCoordinates(
            name = cityName,
            lat = lat,
            lon = lon,
            timezone = timezone,
            timezoneOffset = timezone_offset
        )
    }
}