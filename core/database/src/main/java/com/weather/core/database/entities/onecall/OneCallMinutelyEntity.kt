package com.weather.core.database.entities.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.model.Minutely

@Entity(tableName = "one_call_minutely")
data class OneCallMinutelyEntity(
    val cityName: String,
    val dt: Int,
    val precipitation: Int,
    @PrimaryKey
    val id: Int? = null,
){
    fun asDomainModel(): Minutely {
        return Minutely(
            dt,
            precipitation
        )
    }
}