package com.weather.core.database

import com.weather.core.database.entities.onecall.OneCallEntity
import com.weather.core.database.entities.onecall.meteo.HourlyEntity
import com.weather.core.database.entities.onecall.meteo.WeatherLocationEntity
import com.weather.core.database.entities.relation.CurrentWeatherWithDailyAndHourly
import com.weather.core.database.entities.relation.OneCallAndCurrent
import com.weather.core.database.entities.relation.WeatherAndCurrent
import com.weather.model.ManageLocationsData
import com.weather.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class WeatherLocalDataSource(
	private val dao: WeatherDao,
) {

	fun getWeatherLocations(): Flow<List<WeatherAndCurrent>> {
		return dao.getWeatherLocations().map {
			it.sortedBy { weather ->
				weather.weatherLocation.orderIndex
			}
		}
	}

	fun getAllLocalWeatherData(): Flow<List<OneCallAndCurrent>> {
		return dao.getAllOneCallAndCurrent().map {
			it.sortedBy { oneCallAndCurrent -> oneCallAndCurrent.oneCall.orderIndex }
		}
	}

	suspend fun updateListOrder(locations: List<ManageLocationsData>) {
		val oneCalls = locations.map { location ->
			OneCallEntity(
				location.locationName,
				orderIndex = location.listOrder,
				lat = location.latitude.toDouble(),
				lon = location.longitude.toDouble(),
				timezone = location.timezone,
				timezone_offset = location.timezoneOffset
			)
		}
		dao.insertOneCalls(oneCalls)
	}

//	fun deleteDaily(cityName: String, timeStamp: Long) = dao.deleteDaily(
//		cityName = cityName,
//		timeStamp = timeStamp
//	)
//
	fun deleteHourly(cityName: String, timeStamp: Int) = dao.deleteHourly(
		cityName = cityName,
		timeStamp = timeStamp
	)

	fun databaseIsEmpty(): Boolean = dao.countColumns() == 0

	suspend fun deleteWeatherByCityName(cityNames: List<String>) =
		dao.deleteWeatherByCityName(cityName = cityNames)

	suspend fun deleteWeather(cityName: String) = dao.deleteWeather(cityName = cityName)

	fun getAllWeatherForecast(): Flow<List<CurrentWeatherWithDailyAndHourly>> {
		return dao.getAllWeatherData()
	}

	fun getAllForecastData(): Flow<List<WeatherData>> =
		dao.getAllOneCallWithCurrentAndDailyAndHourly().map { weatherList ->
			weatherList.map { weather ->
				WeatherData(
					weather.oneCall.asDomainModel(),
					weather.current.current.asDomainModel(
						weather.current.weather.map { it ->
							it.asDomainModel()
						}),
					weather.daily.map { it.asDomainModel() },
					weather.hourly.map { it.asDomainModel() }
				)
			}
		}

	fun getLocalWeatherDataByCityName(cityName: String): Flow<WeatherData> = combine(
		dao.getOneCallAndCurrentByCityName(cityName),
		dao.getCurrentWithWeatherByCityName(cityName),
		dao.getDailyByCityName(cityName)
	) { oneCallAndCurrent, currentWeather, daily ->
		val oneCall = oneCallAndCurrent.oneCall.asDomainModel()
		val weather = currentWeather.weather.map {
			it.asDomainModel()
		}
		val current = oneCallAndCurrent.current.asDomainModel(weather)
		WeatherData(
			coordinates = oneCall,
			current = current,
			daily = daily.map { it.asDomainModel() },
			hourly = emptyList()
		)
	}

	suspend fun insertLocalData(
		weatherLocation: WeatherLocationEntity,
		current: com.weather.core.database.entities.onecall.meteo.CurrentEntity,
		daily: List<com.weather.core.database.entities.onecall.meteo.DailyEntity>,
		hourly: List<HourlyEntity>,
	) {
		val dataBaseCount = dao.countColumns()
		val isCityExists =
			dao.checkIfCityExists(cityName = weatherLocation.cityName) == 1
		val orderIndex = if (isCityExists) {
			dao.getWeatherLocation(weatherLocation.cityName).orderIndex
		} else {
			if (dataBaseCount == 0) 0
			else dao.countColumns()
		}
		val newWeather =
			weatherLocation.copy(orderIndex = orderIndex)
		dao.insertData(
			weatherLocation = newWeather,
			current = current,
			daily = daily,
			hourly = hourly
		)
	}
}