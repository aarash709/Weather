package com.experiment.weather.presentation.util

import com.experiment.weather.data.remote.model.geocode.GeoSearchItem

data class GeoSearchState(
    val geoSearchItems: List<GeoSearchItem> = emptyList(),
    val isLoading: Boolean = false
)
