package com.weather.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.weather.entities.onecall.OneCallEntity
import com.weather.entities.onecall.OneCallMinutelyEntity

data class OneCallWithMinutely(
    @Embedded
    val oneCall : OneCallEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val minutely : List<OneCallMinutelyEntity>
)
