package com.weather.core.network

import com.weather.core.network.model.meteoweahter.NetworkCurrent
import com.weather.core.network.model.meteoweahter.NetworkDaily
import com.weather.core.network.model.meteoweahter.NetworkHourly
import com.weather.model.Coordinate
import com.weather.model.geocode.GeoSearchItem

interface RemoteWeatherDatasource {

	suspend fun getCurrent(coordinates: Coordinate): Result<NetworkCurrent>
	suspend fun getDaily(coordinates: Coordinate): Result<NetworkDaily>
	suspend fun getHourly(coordinates: Coordinate): Result<NetworkHourly>
	suspend fun directGeocode(cityName: String): List<GeoSearchItem>
}