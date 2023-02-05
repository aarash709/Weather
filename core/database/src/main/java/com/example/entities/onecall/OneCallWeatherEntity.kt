package com.example.entities.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Weather

@Entity(tableName = "current_weather")
data class OneCallWeatherEntity(
    @PrimaryKey(autoGenerate = false)
    val cityName: String,
    val description: String,
    val icon: String,
    val id: Int,
    val main: String,
){
    fun asDomainModel(): Weather {
        return Weather(
            description = description,
            icon = icon,
            id = id,
            main = main
        )
    }
}