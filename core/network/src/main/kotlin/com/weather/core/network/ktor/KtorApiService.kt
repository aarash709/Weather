package com.weather.core.network.ktor

import com.weather.core.network.BuildConfig.API_KEY
import com.weather.core.network.BuildConfig.BASE_URL
import com.weather.core.network.model.geosearch.GeoSearchItemDto
import com.weather.core.network.model.meteoweahter.NetworkCurrent
import com.weather.core.network.model.meteoweahter.NetworkDaily
import com.weather.core.network.model.meteoweahter.NetworkHourly
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.http.path


const val allWeatherUrl =
	"https://api.open-meteo.com/v1/forecast?latitude=35.6944&longitude=51.4215&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m&hourly=temperature_2m,weather_code,wind_speed_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum&timezone=auto&forecast_days=1"
const val currentParams =
	"temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m"
const val dailyParams =
	"weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,sunrise,sunset,uv_index_max"
const val hourlyParams = "temperature_2m,weather_code,visibility,wind_speed_10m"

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
		location: String,
		limit: String,
		appId: String,
	): List<GeoSearchItemDto>
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
			}
		}.body()
	}

	override suspend fun getGeoSearch(
		location: String,
		limit: String,
		appId: String,
	): List<GeoSearchItemDto> {
		try {
			val geoItems: List<GeoSearchItemDto> = client.get("https://api.openweathermap.org/") {
				url {
					path("geo/1.0/direct")
					parameters.append("q", location)
					parameters.append("limit", "5")
					parameters.append("appid", API_KEY)
				}
			}.body()
			return geoItems

		} catch (e: ClientRequestException) {
			// 4xx - responses
			println("Error: ${e.response.status.description}")
			return emptyList()
		}
	}
}