package com.weather.core.network.ktor

import com.weather.core.network.BuildConfig.BASE_URL
import com.weather.core.network.BuildConfig.SEARCH_URL
import com.weather.core.network.model.geosearch.MeteoSearchItem
import com.weather.core.network.model.meteoweahter.NetworkCurrent
import com.weather.core.network.model.meteoweahter.NetworkDaily
import com.weather.core.network.model.meteoweahter.NetworkHourly
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.http.path


interface KtorApiService {

	/**
	 * @param currentParams are values to include in the json response for example:
	 * "current=temperature_2m,relative_humidity_2m..."
	 * each value should be separated with a ","
	 * visit "https://open-meteo.com/en/docs" for more
	 */
	suspend fun getCurrent(
		lat: String,
		lon: String,
		currentParams: String
	): NetworkCurrent

	/**
	 * @param dailyParams are values to include in the json response for example:
	 * "daily=weather_code,temperature_2m_max..."
	 * each value should be separated with a ","
	 * visit "https://open-meteo.com/en/docs" for more
	 */
	suspend fun getDaily(
		lat: String,
		lon: String,
		dailyParams: String
	): NetworkDaily

	/**
	 * @param hourlyParams are values to include in the json response for example:
	 * "hourly=temperature_2m,weather_code,wind_speed_10m..."
	 * each value should be separated with a ","
	 * visit "https://open-meteo.com/en/docs" for more
	 */
	suspend fun getHourly(
		lat: String,
		lon: String,
		hourlyParams: String
	): NetworkHourly

	suspend fun getGeoSearch(
		cityName: String,
	): Result<MeteoSearchItem>
}

class KtorServiceImpl(
	private val client: HttpClient,
) : KtorApiService {

	override suspend fun getCurrent(
		lat: String,
		lon: String,
		currentParams: String
	): NetworkCurrent {
		return client.get(BASE_URL) {
			url {
				path("v1/forecast")
				parameters.append("latitude", lat)
				parameters.append("longitude", lon)
				parameters.append("current", currentParams)
				parameters.append("timezone", "auto")
			}
		}.body()
	}

	override suspend fun getDaily(lat: String, lon: String, dailyParams: String): NetworkDaily {
		return client.get(BASE_URL) {
			url {
				path("v1/forecast")
				parameters.append("latitude", lat)
				parameters.append("longitude", lon)
				parameters.append("daily", dailyParams)
				parameters.append("timezone", "auto")
			}
		}.body()
	}

	override suspend fun getHourly(lat: String, lon: String, hourlyParams: String): NetworkHourly {
		return client.get(BASE_URL) {
			url {
				path("v1/forecast")
				parameters.append("latitude", lat)
				parameters.append("longitude", lon)
				parameters.append("hourly", hourlyParams)
				parameters.append("timezone", "auto")
				parameters.append("forecast_days", "1")
			}
		}.body()
	}

	override suspend fun getGeoSearch(
		cityName: String,
	): Result<MeteoSearchItem> {
		try {
			val searchItem: MeteoSearchItem = client.get(SEARCH_URL) {
				url {
					path("v1/search")
					parameters.append("name", cityName)
					parameters.append("count", "10")
					parameters.append("language", "en")
					parameters.append("format", "json")
				}
			}.body()
			return Result.success(searchItem)

		} catch (e: ClientRequestException) {
			// 4xx - responses
			println("Error: ${e.response.status.description}")
			return Result.failure(e)
		}
	}
}