package com.weather.core.database.entities.onecall

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.weather.model.OneCallCoordinates

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
    fun asDomainModel(): OneCallCoordinates {
        return OneCallCoordinates(
            name = cityName,
            lat = lat,
            lon = lon,
            timezone = timezone,
            timezone_offset = timezone_offset
        )
    }
}