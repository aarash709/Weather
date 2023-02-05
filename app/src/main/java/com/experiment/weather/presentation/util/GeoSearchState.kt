package com.experiment.weather.presentation.util

import com.example.model.geocode.GeoSearchItem

data class GeoSearchState(
    val geoSearchItems: List<GeoSearchItem> = emptyList(),
    val isLoading: Boolean = false
)
