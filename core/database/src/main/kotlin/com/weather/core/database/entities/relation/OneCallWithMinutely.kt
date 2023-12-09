package com.weather.core.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.core.database.entities.onecall.OneCallEntity
import com.weather.core.database.entities.onecall.OneCallMinutelyEntity

data class OneCallWithMinutely(
    @Embedded
    val oneCall : OneCallEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val minutely : List<OneCallMinutelyEntity>
)
