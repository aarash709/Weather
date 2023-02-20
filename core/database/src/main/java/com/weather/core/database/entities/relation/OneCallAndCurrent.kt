package com.weather.core.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.core.database.entities.onecall.CurrentEntity
import com.weather.core.database.entities.onecall.OneCallEntity

data class OneCallAndCurrent(
    @Embedded
    val oneCall: OneCallEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val current: CurrentEntity
)
