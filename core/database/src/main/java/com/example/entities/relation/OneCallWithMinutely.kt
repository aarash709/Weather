package com.example.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.entities.onecall.OneCallEntity
import com.example.entities.onecall.OneCallMinutelyEntity

data class OneCallWithMinutely(
    @Embedded
    val oneCall : OneCallEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val minutely : List<OneCallMinutelyEntity>
)
