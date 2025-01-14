package com.weather.core.database

import com.weather.core.database.entities.meteo.CurrentEntity
import com.weather.core.database.entities.meteo.DailyEntity
import com.weather.core.database.entities.meteo.HourlyEntity
import com.weather.core.database.entities.meteo.WeatherLocationEntity
import com.weather.core.database.entities.meteo.asDomainModel
import com.weather.core.database.entities.meteo.toCoordinate
import com.weather.core.database.entities.relation.CurrentWeatherWithDailyAndHourly
import com.weather.core.database.entities.relation.WeatherAndCurrent
import com.weather.model.ManageLocationsData
import com.weather.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset

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

	fun getAllLocalWeatherData(): Flow<List<WeatherAndCurrent>> {
		return dao.getAllWeatherAndCurrent().map {
			it.sortedBy { oneCallAndCurrent -> oneCallAndCurrent.weatherLocation.orderIndex }
		}
	}

	suspend fun updateListOrder(locations: List<ManageLocationsData>) {
		val oneCalls = locations.map { location ->
			WeatherLocationEntity(
				location.locationName,
				orderIndex = location.listOrder,
				lat = location.latitude.toDouble(),
				lon = location.longitude.toDouble(),
				timezone = location.timezone,
				timezoneOffset = location.timezoneOffset
			)
		}
		dao.insertWeatherLocations(oneCalls)
	}

	fun deleteDaily(cityName: String, timeStamp: String) = dao.deleteDaily(
		cityName = cityName,
		timeStamp = timeStamp
	)

	fun deleteHourly(cityName: String, timeStamp: String) = dao.deleteHourly(
		cityName = cityName,
		timeStamp = timeStamp
	)

	fun databaseIsEmpty(): Boolean = dao.countColumns() == 0

	suspend fun deleteWeatherByCityName(cityNames: List<String>) =
		dao.deleteMultipleWeather(cityName = cityNames)

	suspend fun deleteWeather(cityName: String) = dao.deleteOneWeather(cityName = cityName)

	fun getAllWeatherForecast(): Flow<List<CurrentWeatherWithDailyAndHourly>> {
		return dao.getAllWeatherData()
	}

	fun getAllForecastData(): Flow<List<WeatherData>> =
		dao.getAllWeatherData().map { weatherList ->
			weatherList.map { weather ->
				val current = weather.current
				val daily = weather.daily
				val hourly = weather.hourly
				val sunrise =
					LocalDateTime.parse(daily.first().sunrise).toEpochSecond(ZoneOffset.UTC).toInt()
				val sunset =
					LocalDateTime.parse(daily.first().sunset).toEpochSecond(ZoneOffset.UTC).toInt()
				WeatherData(
					coordinates = weather.weatherLocation.toCoordinate(cityName = current.cityName),
					current = current.asDomainModel(
						visibility = hourly.first().visibility.toInt(),
						uvi = daily.first().uvIndex,
						sunrise = sunrise,
						sunset = sunset
					),
					daily = daily.map { it.asDomainModel() },
					hourly = hourly.map {
						it.asDomainModel(
							humidity = 0,
							pressure = current.pressureMsl.toInt(),
							daily.first().uvIndex,
							windDirection = 12
						)
					},
				)
			}
		}

	fun getLocalWeatherDataByCityName(cityName: String): Flow<WeatherData> =
		dao.getAllWeatherData()
			.filter { it.first().weatherLocation.cityName == cityName }
			.map { weatherList ->
				val weather = weatherList.first()
				val current = weather.current
				val daily = weather.daily
				val hourly = weather.hourly
//				val sunrise = Instant.parse(daily.first().sunrise).epochSecond.toInt()
				val sunrise =
					LocalDateTime.parse(daily.first().sunrise).toEpochSecond(ZoneOffset.UTC).toInt()
//				val sunset = Instant.parse(daily.first().sunset).epochSecond.toInt()
				val sunset =
					LocalDateTime.parse(daily.first().sunset).toEpochSecond(ZoneOffset.UTC).toInt()
				WeatherData(
					coordinates = weather.weatherLocation.toCoordinate(cityName = current.cityName),
					current = current.asDomainModel(
						visibility = hourly.first().visibility.toInt(),
						uvi = daily.first().uvIndex,
						sunrise = sunrise,
						sunset = sunset
					),
					daily = daily.map { it.asDomainModel() },
					hourly = hourly.map {
						it.asDomainModel(
							humidity = 0,
							pressure = current.pressureMsl.toInt(),
							daily.first().uvIndex,
							12
						)
					},
				)
			}

	suspend fun insertLocalData(
		weatherLocation: WeatherLocationEntity,
		current: CurrentEntity,
		daily: List<DailyEntity>,
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