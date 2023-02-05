package com.weather.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.entities.onecall.OneCallCurrentEntity
import com.weather.entities.onecall.OneCallWeatherEntity

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
