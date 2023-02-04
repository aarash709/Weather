package com.experiment.weather.data.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.experiment.weather.data.database.entities.onecall.OneCallEntity
import com.experiment.weather.data.database.entities.onecall.OneCallMinutelyEntity

data class OneCallWithMinutely(
    @Embedded
    val oneCall : OneCallEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val minutely : List<OneCallMinutelyEntity>
)
