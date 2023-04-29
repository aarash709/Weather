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
    val country: String? = null,
    val lat: Double? = null,
    val local_names: LocalNames? = null,
    val lon: Double? = null,
    val name: String? = null,
    val state: String? = null,
) {
    companion object {
        val empty = List(4) {
            GeoSearchItem(
                country = null,
                lat = null,
                local_names = null,
                lon = null,
                name = null,
                state = null,
            )
        }
    }
}
