package com.weather.core.network

import com.experiment.weather.core.common.extentions.WeatherCoroutineDispatchers
import com.experiment.weather.core.common.extentions.WeatherDispatchers
import com.weather.core.network.model.meteoweahter.Current
import com.weather.core.network.model.meteoweahter.CurrentUnits
import com.weather.core.network.model.meteoweahter.NetworkCurrent
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FakeWeatherRemoteDataSource(
	@WeatherCoroutineDispatchers(WeatherDispatchers.IO) val dispatcher: CoroutineContext
) {
	suspend fun getRemoteData(): Result<NetworkCurrent> {
		return withContext(dispatcher) {
			Result.success(
				value = NetworkCurrent(
					elevation = 86.87,
					generationTimeMs = 88.89,
					latitude = 90.91,
					longitude = 92.93,
					timezone = "sagittis",
					timezoneAbbreviation = "ne",
					utcOffsetSeconds = 2011,
					currentUnits = CurrentUnits(
						apparentTemperature = "tempor",
						interval = "vim",
						isDay = "vituperata",
						precipitation = "varius",
						pressureMsl = "tale",
						relativeHumidity2m = "tractatos",
						surfacePressure = "phasellus",
						temperature2m = "doctus",
						time = "melius",
						weatherCode = "hac",
						windDirection10m = "dico",
						windSpeed10m = "no"
					),
					current = Current(
						apparentTemperature = 94.95,
						interval = 8725,
						isDay = 8925,
						precipitation = 96.97,
						pressureMsl = 98.99,
						relativeHumidity2m = 5104,
						surfacePressure = 100.101,
						temperature2m = 102.103,
						time = "liber",
						weatherCode = 8556,
						windDirection10m = 2997,
						windSpeed10m = 104.105
					)
				)
			)
		}
	}

	suspend fun directGeocode(cityName: String): List<GeoSearchItem> {
		return withContext(dispatcher) {
			listOf(
				GeoSearchItem(
					country = "iran",
					lat = 123.0,
					local_names = null,
					lon = 123.123,
					name = "tehran",
					state = "tehranprov"
				)
			)
		}
	}

}