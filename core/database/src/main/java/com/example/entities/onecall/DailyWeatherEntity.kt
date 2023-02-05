package com.example.entities.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "one_call_daily_weather")
data class DailyWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val rowId: Int,
    val cityName : String,
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

