package com.weather.feature.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import com.experiment.weather.core.common.extentions.convertToUserSettings
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.SavableForecastData
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.TemperatureUnits.C
import com.weather.model.WeatherData
import com.weather.model.WindSpeedUnits
import com.weather.model.WindSpeedUnits.KM
import com.weather.model.hourly
import com.weather.sync.work.utils.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.Instant
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
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val cityName = savedStateHandle.get<String>("cityName").orEmpty()
    private val _timeOfDay = MutableStateFlow(TimeOfDay.Day)
    val timeOfDay = _timeOfDay
    internal var isSyncing = syncStatus.isSyncing
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), false)


    @ExperimentalCoroutinesApi
    val weatherUIState = getWeatherData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = SavableForecastData.placeholderDefault
    )

    private fun getWeatherData(): Flow<SavableForecastData> {
        return weatherRepository.getAllForecastWeatherData()
            .combine(getFavoriteCityCoordinate()) { allWeather, favoriteCityName ->
                Timber.e("cityName: $favoriteCityName")
                if (favoriteCityName.isNullOrBlank() ||
                    allWeather.all { it.coordinates.name != favoriteCityName }
                )
                    allWeather.first()
                else
                    allWeather.first { it.coordinates.name == favoriteCityName }
            }
            .flowOn(Dispatchers.IO)
            .combine(getUserSettings()) { weather, userSettings ->
                userSettings.setDefaultIfNull()
                val newWeather = weather.convertToUserSettings(userSettings = userSettings)
                val hourlyData = newWeather.hourly
                val mutableHourly = newWeather.hourly.toMutableList()
//                hourlyData.forEachIndexed { index, hourly ->
//                    val date = SimpleDateFormat("HH:mm", Locale.getDefault())
//                    if ((date.parse(hourly.dt)?.time ?: 1) /1000 <= newWeather.current.sunset) {
//                        val hourlyBeforeSunrise = hourlyData.elementAt(index).copy(dt = "Sunset")
//                        mutableHourly.add(index, hourlyBeforeSunrise)
//                    }
//                }
                newWeather.copy {
                    WeatherData.hourly set mutableHourly.toList()
                }
                SavableForecastData(
                    weather = newWeather,
                    userSettings = userSettings,
                    showPlaceHolder = false
                )
            }
            .onEach { forecastData ->
                val currentForecastTime = forecastData.weather.current.dt
                val current = forecastData.weather.current
                _timeOfDay.update {
                    calculateTimeOfDay(
                        currentForecastTime = currentForecastTime.toLong(),
                        sunrise = current.sunrise.toLong(),
                        sunset = current.sunset.toLong(),
                        30
                    )
                }
                val coordinate = Coordinate(
                    forecastData.weather.coordinates.name,
                    forecastData.weather.coordinates.lat.toString(),
                    forecastData.weather.coordinates.lon.toString()
                )
                if (isDataExpired(dataTimestamp = currentForecastTime, minutesThreshold = 30)) {
                    sync(coordinate)
                }
            }
            .retry(2)
            .catch {
                Timber.e("data state:${it.message}")
                Timber.e("data state:${it.cause}")
            }
    }

    private fun getFavoriteCityCoordinate(): Flow<String?> {
        return userRepository.getFavoriteCityCoordinate()
    }

    fun sync(savedCityCoordinate: Coordinate) {
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
                SettingsData(windSpeedUnits = wind, temperatureUnits = temp)
            }
    }

    internal fun isDataExpired(dataTimestamp: Int, minutesThreshold: Int): Boolean {
        val currentTime = Instant.now().epochSecond
        val differanceInSeconds = currentTime.minus(dataTimestamp)
        val differanceInMinutes = Duration.ofSeconds(differanceInSeconds).toMinutes()
        Timber.e((differanceInMinutes > minutesThreshold).toString())
        return differanceInMinutes > minutesThreshold
    }

    private suspend fun SettingsData.setDefaultIfNull(
        defaultWindSpeedUnits: WindSpeedUnits = KM,
        defaultTemperature: TemperatureUnits = C,
    ) {
        windSpeedUnits ?: userRepository.setWindSpeedUnitSetting(defaultWindSpeedUnits)
        temperatureUnits ?: userRepository.setTemperatureUnitSetting(defaultTemperature)
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
}