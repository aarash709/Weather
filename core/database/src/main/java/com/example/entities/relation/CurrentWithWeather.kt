package com.example.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.entities.onecall.OneCallCurrentEntity
import com.example.entities.onecall.OneCallWeatherEntity

data class CurrentWithWeather(
    @Embedded
    val current : OneCallCurrentEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val weather : List<OneCallWeatherEntity>
){

//    fun asDomainModel(): List<Weather>{
//        return listOf(
//
//        )
//    }

}
