package com.experiment.weather.presentation.util

import com.weather.model.geocode.GeoSearchItem


data class GeoSearchState(
    val geoSearchItems: List<GeoSearchItem> = emptyList(),
    val isLoading: Boolean = false
)
