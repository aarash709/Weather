package com.weather.core.repository

import com.weather.core.database.WeatherLocalDataSource
import com.weather.core.network.WeatherRemoteDatasourceImpl
import com.weather.core.network.model.meteoweahter.toDailyPreview
import com.weather.model.Coordinate
import com.weather.model.DailyPreview
import com.weather.model.ManageLocationsData
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
	private val remoteWeather: WeatherRemoteDatasourceImpl,
	private val localWeather: WeatherLocalDataSource,
) : WeatherRepository {
	override suspend fun deleteWeatherByCityName(cityNames: List<String>) {
		localWeather.deleteWeatherByCityName(cityNames = cityNames)
	}

	override fun isDatabaseEmpty(): Boolean = localWeather.databaseIsEmpty()

	override suspend fun getFiveDay(coordinate: Coordinate): List<DailyPreview> {
		return try {
			val data = remoteWeather.getDaily(coordinates = coordinate)
			val daily = data.getOrNull()!!.daily
			daily.toDailyPreview()
		} catch (e: IOException) {
			Timber.e("getFiveDay error: ${e.message}")
			listOf()
		}
	}

	override fun getAllForecastWeatherData(): Flow<List<WeatherData>> {
		return localWeather.getAllForecastData()
	}


	override fun getAllWeatherLocations(): Flow<List<ManageLocationsData>> {
		return localWeather.getAllLocalWeatherData()
			.map { weatherList ->
				weatherList.map { data ->
					val oneCall = data.weatherLocation
					val current = data.current
					ManageLocationsData(
						locationName = oneCall.cityName,
						weatherIcon = "current.icon",
						latitude = oneCall.lat.toString(),
						longitude = oneCall.lon.toString(),
						timezone = oneCall.timezone,
						timezoneOffset = oneCall.timezoneOffset,
						currentTemp = current.temperature2m.toString(),
						humidity = current.relativeHumidity2m.toString(),
						feelsLike = current.apparentTemperature.toString(),
						listOrder = oneCall.orderIndex!!,
					)
				}
			}
	}

	override suspend fun reorderData(locations: List<ManageLocationsData>) {
		localWeather.updateListOrder(locations)
	}

	override fun getLocalWeatherByCityName(cityName: String): Flow<WeatherData> {
		return localWeather.getLocalWeatherDataByCityName(cityName = cityName)
	}

	override suspend fun syncWeather(coordinate: Coordinate) {
		//work in progress
		try {
			val remoteCurrent = remoteWeather
				.getCurrent(
					coordinates = coordinate
				)
				.getOrNull()
			val locationInfo = remoteCurrent
				?.toLocationEntity(
					cityName = coordinate.cityName!!,
					coordinate.latitude.toDouble(),
					coordinate.longitude.toDouble()
				)
			val daily = remoteWeather
				.getDaily(coordinate)
				.getOrNull()
				?.daily
				?.toEntity(coordinate.cityName!!)
				?.slice(0..4)

			val hourly = remoteWeather
				.getHourly(coordinate)
				.getOrNull()
				?.hourly
				?.toEntity(coordinate.cityName!!)
			localWeather.insertLocalData(
				weatherLocation = locationInfo!!,
				current = remoteCurrent.toEntity(cityName = coordinate.cityName!!),
				daily = daily!!,
				hourly = hourly!!
			)
			val firstDailyTimeStamp = daily.first().time
			localWeather.deleteDaily(
				cityName = coordinate.cityName!!,
				timeStamp = firstDailyTimeStamp
			)
			//subtract current database time by 1 hour then delete data older than specified time
				LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).minusHours(1)
			val dataTime = LocalDateTime.parse(remoteCurrent.current.time).minusHours(1).format(
				DateTimeFormatter.ISO_DATE_TIME)
			localWeather.deleteHourly(
				cityName = coordinate.cityName!!,
				timeStamp = dataTime
			)
		} catch (e: Exception) {
			Timber.e("sync error: ${e.message}")
		}
	}

	override fun searchLocation(cityName: String): Flow<List<GeoSearchItem>> =
		flow {
			val remoteData = remoteWeather.directGeocode(cityName = cityName)
			if (remoteData.isNotEmpty())
				emit(remoteData)
		}.catch {
			Timber.e("search error: ${it.message}")
		}

}