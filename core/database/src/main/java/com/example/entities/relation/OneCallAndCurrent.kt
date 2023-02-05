package com.example.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.entities.onecall.OneCallCurrentEntity
import com.example.entities.onecall.OneCallEntity

data class OneCallAndCurrent(
    @Embedded
    val oneCall: OneCallEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val current: OneCallCurrentEntity
)
