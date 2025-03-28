package com.weather.core.network.model.geosearch


import com.weather.model.geocode.GeoSearchItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeteoSearchItem(
	@SerialName("generationtime_ms")
	val generationTimeMs: Double,
	@SerialName("results")
	val results: List<Result>
)

fun MeteoSearchItem.toGeoSearchItem(): List<GeoSearchItem> {
	return results.map {
		GeoSearchItem(
			country = it.country ?: "",
			lat = it.latitude ?: 0.0,
			lon = it.longitude ?: 0.0,
			name = it.name ?: ""

		)
	}
}