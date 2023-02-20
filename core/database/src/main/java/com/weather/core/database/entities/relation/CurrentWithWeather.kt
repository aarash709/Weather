package com.weather.core.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.core.database.entities.onecall.CurrentEntity
import com.weather.core.database.entities.onecall.CurrentWeatherEntity

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
