package com.weather.core.repository

import com.weather.core.database.WeatherLocalDataSource
import com.weather.core.network.WeatherRemoteDatasourceImpl
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
	private val remoteWeather: WeatherRemoteDatasourceImpl,
	private val localWeather: WeatherLocalDataSource,
) : WeatherRepository {
	override suspend fun deleteWeatherByCityName(cityNames: List<String>) {
		localWeather.deleteWeatherByCityName(cityNames = cityNames)
	}

	override fun isDatabaseEmpty(): Boolean = localWeather.databaseIsEmpty()

//    override suspend fun getFiveDay(coordinate: Coordinate): List<DailyPreview> {
//        return try {
//            val data = remoteWeather.getRemoteData(coordinates = coordinate, exclude = "")
//            val daily = data.data!!.daily.slice(0..4)
//            daily.map {
//                DailyPreview(
//                    it.temp.day.toInt(),
//                    it.temp.night.toInt(),
//                    it.dt.toString(),
//                    it.weather[0].icon,
//                    it.weather[0].description
//                )
//            }
//        } catch (e: IOException) {
//            Timber.e("getFiveDay error: ${e.message}")
//            listOf()
//        }
//    }

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

			val hourly = remoteWeather
				.getHourly(coordinate)
				.getOrNull()
				?.hourly
				?.toEntity(coordinate.cityName!!)
			Timber.e("dt: ${remoteCurrent?.timezone}")
			Timber.e("dt: ${remoteCurrent?.current?.apparentTemperature}")
			localWeather.insertLocalData(
				weatherLocation = locationInfo!!,
				current = remoteCurrent.toEntity(cityName = coordinate.cityName!!),
				daily = daily!!,
				hourly = hourly!!
			)
			val firstDailyTimeStamp = remoteCurrent.current.time
			val firstHourlyTimeStamp = hourly.first().time
			Timber.e(firstHourlyTimeStamp)
			localWeather.deleteDaily(
				cityName = coordinate.cityName!!,
				timeStamp = firstDailyTimeStamp
			)
			localWeather.deleteHourly(
				cityName = coordinate.cityName!!,
				timeStamp = firstHourlyTimeStamp
			)
//			remoteWeatherInfo.let {
			//                localWeather.insertLocalData(
//                    oneCall = remoteWeatherInfo.data!!.toEntity(cityName = coordinate.cityName.toString()),
//                    current = remoteWeatherInfo.data!!.current.toEntity(cityName = coordinate.cityName.toString()),
//                    currentWeather = remoteWeatherInfo.data!!.current.weather.map {
//                        it.toEntity(cityName = coordinate.cityName.toString())
//                    },
//                    daily = remoteWeatherInfo.data!!.daily.slice(0..4)
//                        .map { it.toEntity(cityName = coordinate.cityName.toString()) },
//                    hourly = remoteWeatherInfo.data!!.hourly.slice(0..12).map {
//                        it.toEntity(cityName = coordinate.cityName.toString())
//                    }
//                )
//
//			}
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

	override suspend fun getCurrent(latitude: String, longitude: String, params: String) {
		TODO("Not yet implemented")
	}

	override suspend fun getDaily(latitude: String, longitude: String, params: String) {
		TODO("Not yet implemented")
	}

	override suspend fun getHourly(latitude: String, longitude: String, params: String) {
		TODO("Not yet implemented")
	}

	override suspend fun syncWeather(cityName: String, coordinate: Coordinate) {
		TODO("Not yet implemented")
	}
}