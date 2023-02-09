package com.weather.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.entities.onecall.CurrentEntity
import com.weather.entities.onecall.CurrentWeatherEntity

data class CurrentWithWeather(
    @Embedded
    val current : CurrentEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val weather : List<CurrentWeatherEntity>
){
//    fun asDomainModel(): List<Weather>{
//        return listOf(
//
//        )
//    }
}
