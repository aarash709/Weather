package com.example.entities.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "one_call_fells_like")
data class FeelsLikeEntity(
    @PrimaryKey(autoGenerate = false)
    val cityName : String,
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)
