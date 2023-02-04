package com.experiment.weather.data.remote.model.geocode

data class GeoSearchItem(
    val country: String? = null,
    val lat: Double? = null,
    val local_names: LocalNames? = null,
    val lon: Double? = null,
    val name: String? = null,
    val state: String? = null,
)
