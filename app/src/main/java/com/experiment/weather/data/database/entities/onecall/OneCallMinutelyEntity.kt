package com.experiment.weather.data.database.entities.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.experiment.weather.data.remote.dto.oncecall.MinutelyDto

@Entity(tableName = "one_call_minutely")
data class OneCallMinutelyEntity(
    val cityName: String,
    val dt: Int,
    val precipitation: Int,
    @PrimaryKey
    val id: Int? = null,
){
    fun asDomainModel(): MinutelyDto{
        return MinutelyDto(
            dt,
            precipitation
        )
    }
}