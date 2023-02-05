package com.example.entities.onecall

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Minutely

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