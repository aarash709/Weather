package com.weather.model.geocode

data class SavableSearchState(
    val geoSearchItems: List<GeoSearchItem>,
    val showPlaceholder: Boolean = true,
) {
    companion object {
        val empty = SavableSearchState(
            geoSearchItems = GeoSearchItem.empty,
            showPlaceholder = true
        )
    }
}

data class GeoSearchItem(
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val name: String = "",
) {
    companion object {
        val empty = List(4) {
            GeoSearchItem()
        }
    }
}
