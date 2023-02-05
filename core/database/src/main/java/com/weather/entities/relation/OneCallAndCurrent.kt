package com.weather.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.entities.onecall.OneCallCurrentEntity
import com.weather.entities.onecall.OneCallEntity

data class OneCallAndCurrent(
    @Embedded
    val oneCall: OneCallEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val current: OneCallCurrentEntity
)
