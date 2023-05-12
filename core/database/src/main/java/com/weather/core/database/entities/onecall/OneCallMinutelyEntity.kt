package com.weather.core.database.entities.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "one_call_minutely")
data class OneCallMinutelyEntity(
    val cityName: String,
    val dt: Int,
    val precipitation: Int,
    @PrimaryKey
    val id: Int? = null,
)