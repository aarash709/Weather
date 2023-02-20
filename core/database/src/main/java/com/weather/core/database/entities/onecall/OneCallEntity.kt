package com.weather.core.database.entities.onecall

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.weather.model.OneCallCoordinates

@Entity(tableName = "one_call", foreignKeys = [ForeignKey(
    entity = OneCallEntity::class,
    parentColumns = arrayOf("cityName"),
    childColumns = arrayOf("cityName"),
    onDelete = ForeignKey.CASCADE,
    deferred = true
)])
data class OneCallEntity(
    @PrimaryKey(autoGenerate = false)
    val cityName: String,
    val lat: Double?,
    val lon: Double? = null,
    val timezone: String? = null,
    val timezone_offset: Int? = null,
    ){
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