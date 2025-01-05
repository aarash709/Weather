package com.weather.core.network

import com.weather.core.network.BuildConfig.API_KEY
import com.weather.core.network.ktor.KtorApiService
import com.weather.core.network.ktor.currentParams
import com.weather.core.network.ktor.dailyParams
import com.weather.core.network.ktor.hourlyParams
import com.weather.core.network.model.meteoweahter.NetworkCurrent
import com.weather.core.network.model.meteoweahter.NetworkDaily
import com.weather.core.network.model.meteoweahter.NetworkHourly
import com.weather.model.Coordinate
import com.weather.model.geocode.GeoSearchItem
import kotlinx.io.IOException
import timber.log.Timber

class WeatherRemoteDatasourceImpl(
	private val ktorApi: KtorApiService,
) : RemoteWeatherDatasource {

	override suspend fun getCurrent(coordinates: Coordinate): Result<NetworkCurrent> {
		return try {
            val current = ktorApi.getCurrent(
                lat = coordinates.latitude,
                lon = coordinates.longitude,
				currentParams = currentParams
            )
            Result.success(current)
        } catch (e: IOException) {
            Timber.e("IO exeption ${e.message}")
            Result.failure(IOException("Connectivity issue!"))
        }
	}

	override suspend fun getDaily(coordinates: Coordinate): Result<NetworkDaily> {
			return try {
			val current = ktorApi.getDaily(
				lat = coordinates.latitude,
				lon = coordinates.longitude,
				dailyParams = dailyParams
			)
			Result.success(current)
		} catch (e: IOException) {
			Timber.e("IO exeption ${e.message}")
			Result.failure(IOException("Connectivity issue!"))
		}

		}

	override suspend fun getHourly(coordinates: Coordinate): Result<NetworkHourly> {
		return try {
			val current = ktorApi.getHourly(
				lat = coordinates.latitude,
				lon = coordinates.longitude,
				hourlyParams = hourlyParams
			)
			Result.success(current)
		} catch (e: IOException) {
			Timber.e("IO exeption ${e.message}")
			Result.failure(IOException("Connectivity issue!"))
		}

	}

	override suspend fun directGeocode(cityName: String): List<GeoSearchItem> {
		return try {
			ktorApi.getGeoSearch(location = cityName, limit = "5", API_KEY).map {
				it.toGeoSearchItem()
			}
		} catch (e: Exception) {
			Timber.e("direct geo error: ${e.message}")
			return emptyList()
		}
	}

}