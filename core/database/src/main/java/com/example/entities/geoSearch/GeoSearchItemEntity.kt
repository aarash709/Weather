package com.example.entities.geoSearch

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeoSearchItemEntity(
    val country: String,
    val lat: Double,
    @Embedded
    val local_names: LocalNamesEntity,
    val lon: Double,
    val name: String,
    @PrimaryKey
    val id: Int? = null
)
