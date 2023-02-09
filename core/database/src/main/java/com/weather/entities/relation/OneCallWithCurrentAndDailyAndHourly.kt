package com.weather.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.entities.onecall.DailyEntity
import com.weather.entities.onecall.CurrentEntity
import com.weather.entities.onecall.OneCallEntity
import com.weather.entities.onecall.OneCallHourlyEntity
import com.weather.model.Hourly

class OneCallWithCurrentAndDailyAndHourly(
    @Embedded
    val oneCall: OneCallEntity,
    @Relation(
        entity = CurrentEntity::class,
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val current: CurrentWithWeather,
    @Relation(
        entity = DailyEntity::class,
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val daily: List<DailyEntity>,
    @Relation(
        entity = OneCallHourlyEntity::class,
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val hourly: List<OneCallHourlyEntity>,
)
