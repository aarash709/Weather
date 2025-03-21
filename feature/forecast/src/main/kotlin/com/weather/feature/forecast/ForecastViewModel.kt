package com.weather.feature.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.weather.core.common.extentions.applySettings
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.core.repository.data.getWeatherCondition
import com.weather.model.Coordinate
import com.weather.model.Hourly
import com.weather.model.SavableForecastData
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits.C
import com.weather.model.WindSpeedUnits.KM
import com.weather.sync.work.utils.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

enum class TimeOfDay {
	Day,
	Night,
	Dawn
}

@HiltViewModel
class ForecastViewModel @Inject constructor(
	private val syncStatus: SyncManager,
	private val weatherRepository: WeatherRepository,
	private val userRepository: UserRepository,
//    savedStateHandle: SavedStateHandle,
) : ViewModel() {

	//    private val cityName = savedStateHandle.get<String>("cityName").orEmpty()
	private val _timeOfDay = MutableStateFlow(TimeOfDay.Day)
	internal val timeOfDay = _timeOfDay
	internal var isSyncing = syncStatus.isSyncing
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), false)

	internal val userSettings = getUserSettings().stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(1000),
		initialValue = SettingsData()
	)
	internal val favoriteCity = getFavoriteCityCoordinate().stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(1000),
		initialValue = ""
	)

	@ExperimentalCoroutinesApi
	internal val weatherUIState = getAllLocationsData().stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(1000),
		initialValue = listOf(SavableForecastData.placeholderDefault)
	)

	private fun getAllLocationsData(): Flow<List<SavableForecastData>> {
		return combine(
			weatherRepository.getAllForecastWeatherData(),
			getFavoriteCityCoordinate(),
			getUserSettings()
		) { allWeather, favoriteCity, setting ->
			allWeather.map { weather ->
				val newWeather = weather.applySettings(userSettings = setting)
				val daily = newWeather.daily.map {
					val weatherCondition =
						getWeatherCondition(
							wmoCode = it.weatherCode.toString(),
							isDay = 1
						) // 1 is day time to fetch day icons
					it.copy(iconUrl = weatherCondition.image)
				}
				val hourly = newWeather.hourly.map {
					val weatherCondition =
						getWeatherCondition(wmoCode = it.weatherCode.toString(), isDay = it.isDay)
					it.copy(iconUrl = weatherCondition.image)
				}
				val current = newWeather.current.copy(
					condition = getWeatherCondition(
						wmoCode = daily.first().weatherCode.toString(),
						isDay = 1
					).description
				)
				val timezoneOffset = newWeather.coordinates.timezoneOffset.toLong()

				/*val hourlyData = calculateSunriseAndSunset(
					hourlyData = hourly,
					sunrise = current.sunrise,
					sunset = current.sunset,
					timezoneOffset = timezoneOffset
				)*/
				SavableForecastData(
					weather = newWeather.copy(
						current = current,
						hourly = hourly,
						daily = daily
					)
				)
			}
		}
			.onEach { allForecast ->
				allForecast.map { data ->
					val current = data.weather.current
					val dataCurrentDateTimeString = current.time
					_timeOfDay.update {
						it
//						calculateTimeOfDay(
//							currentForecastTime = currentForecastTime.toLong(),
//							sunrise = current.sunrise.toLong(),
//							sunset = current.sunset.toLong(),
//							30
//						)
						//todo needs fix
					}
					val coordinate = Coordinate(
						data.weather.coordinates.name,
						data.weather.coordinates.lat.toString(),
						data.weather.coordinates.lon.toString()
					)
					if (isDataExpired(
							dataTimestamp = dataCurrentDateTimeString,
							dataTimeOffset = data.weather.coordinates.timezoneOffset,
							triggerThreshold = 30
						)
					) {
						sync(coordinate)
					}
				}
			}
			.retry(2)
			.catch {
				Timber.e("data error:${it.message}")
			}
	}

	private fun getFavoriteCityCoordinate(): Flow<String?> {
		return userRepository.getFavoriteCityCoordinate()
	}

	internal fun sync(savedCityCoordinate: Coordinate) {
		viewModelScope.launch {
			val coordinate = Coordinate(
				cityName = savedCityCoordinate.cityName,
				latitude = savedCityCoordinate.latitude,
				longitude = savedCityCoordinate.longitude
			)
			syncStatus.syncWithCoordinate(coordinate)
		}
	}

	internal fun getUserSettings(): Flow<SettingsData> {
		return userRepository.getTemperatureUnitSetting()
			.combine(userRepository.getWindSpeedUnitSetting()) { temp, wind ->
				//default settings if not set by user yet || null
				val winUnit = wind ?: KM
				val tempUnit = temp ?: C
				SettingsData(windSpeedUnits = winUnit, temperatureUnits = tempUnit)
			}
	}

	/**
	 * timestamp is iso8601 standard
	 * @param triggerThreshold is the time in minutes specified after which
	 * we can start to sync data.
	 */
	internal fun isDataExpired(
		dataTimestamp: String,
		dataTimeOffset: Int,
		triggerThreshold: Int
	): Boolean {
		val currentTime = (Instant.now().epochSecond).plus(dataTimeOffset)
		val dataTimeStampSeconds = LocalDateTime.parse(dataTimestamp).toEpochSecond(ZoneOffset.UTC)
		val differanceInSeconds = currentTime.minus(dataTimeStampSeconds)
		val differanceInMinutes = Duration.ofSeconds(differanceInSeconds).toMinutes()
		return differanceInMinutes > triggerThreshold
	}

	private fun calculateTimeOfDay(
		currentForecastTime: Long,
		sunrise: Long,
		sunset: Long,
		dawnMinutesThreshold: Int = 30,
	): TimeOfDay {
		val sunriseDifference = Duration.ofSeconds(currentForecastTime - sunrise).toMinutes()
		val sunsetDifference = -Duration.ofSeconds(currentForecastTime - sunset).toMinutes()
		Timber.e("$sunriseDifference")
		Timber.e("$sunsetDifference")
		return when {
			currentForecastTime > sunset || currentForecastTime < sunrise -> TimeOfDay.Night
			sunriseDifference < dawnMinutesThreshold -> TimeOfDay.Dawn
			sunsetDifference < dawnMinutesThreshold -> TimeOfDay.Dawn
			else -> TimeOfDay.Day
		}
	}

	/**
	 * sunrise and sunset will be iso8601 standard
	 * @param timezoneOffset is in seconds
	 */
	private fun calculateSunriseAndSunset(
		hourlyData: List<Hourly>,
		sunrise: Int,
		sunset: Int,
		timezoneOffset: Long,
	): List<Hourly> {
		val mutableHourly = hourlyData.toMutableList()
		val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
		formatter.timeZone = TimeZone.getTimeZone("GMT")
		val sunsetOffset = sunset.toLong().plus(timezoneOffset)
		val sunriseOffset = sunrise.toLong().plus(timezoneOffset)
		val formattedSunset = formatter.format(Date(sunsetOffset * 1000))
		val formattedSunrise = formatter.format(Date(sunriseOffset * 1000))

		hourlyData.lastOrNull {
			val weatherTimeSeconds = Instant.parse(it.time).epochSecond
			weatherTimeSeconds <= sunset
		}
			?.let { hourly ->
				val index = hourlyData.indexOf(hourly)
				val sunsetHourly = hourly.copy(
					sunriseSunset = "Sunset",
					time = formattedSunset,
//                    dt = sunset
				)
				mutableHourly.add(index + 1, sunsetHourly)
			}
		hourlyData.lastOrNull {
			val weatherTimeSeconds = Instant.parse(it.time).epochSecond
			weatherTimeSeconds <= sunrise
		}
			?.let { hourly ->
				val index = hourlyData.indexOf(hourly)
				val sunsetHourly = hourly.copy(
					sunriseSunset = "Sunrise",
					time = formattedSunrise,
//                    dt = sunrise
				)
				mutableHourly.add(index + 1, sunsetHourly)
			}
		return mutableHourly.toList()
	}

}