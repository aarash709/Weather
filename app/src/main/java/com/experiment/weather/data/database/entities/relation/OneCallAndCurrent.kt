package com.experiment.weather.data.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.experiment.weather.data.database.entities.onecall.OneCallCurrentEntity
import com.experiment.weather.data.database.entities.onecall.OneCallEntity

data class OneCallAndCurrent(
    @Embedded
    val oneCall: OneCallEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val current: OneCallCurrentEntity
)
