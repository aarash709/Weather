package com.weather.feature.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.weather.core.common.extentions.convertToUserSettings
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.SavableForecastData
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.TemperatureUnits.C
import com.weather.model.WindSpeedUnits
import com.weather.model.WindSpeedUnits.KM
import com.weather.sync.work.utils.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val syncStatus: SyncManager,
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val cityName = savedStateHandle.get<String>("cityName").orEmpty()

    private val _dataBaseOrCityIsEmpty = MutableStateFlow(false)
    val dataBaseOrCityIsEmpty = _dataBaseOrCityIsEmpty.asStateFlow()

    internal var isSyncing = syncStatus.isSyncing
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), false)


    @ExperimentalCoroutinesApi
    val weatherUIState = getWeatherData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = SavableForecastData.placeholderDefault
    )

    init {
        checkDatabaseIsEmpty()
    }

    private fun getWeatherData(): Flow<SavableForecastData> {
        return weatherRepository.getAllForecastWeatherData()
            .combine(getFavoriteCityCoordinate()) { allWeather, coordinate ->
                Timber.e("cityName: ${coordinate?.cityName}")
                if (coordinate?.cityName.isNullOrBlank() ||
                    allWeather.all { it.coordinates.name != coordinate?.cityName }
                )
                    allWeather.first()
                else
                    allWeather.first { it.coordinates.name == coordinate?.cityName }
            }
            .flowOn(Dispatchers.IO)
            .combine(getUserSettings()) { weather, userSettings ->
                userSettings.setDefaultIfNull()
                val current = weather.current.convertToUserSettings(
                    userSettings.temperatureUnits,
                    userSettings.windSpeedUnits
                )
                val daily = weather.daily.map {
                    it.convertToUserSettings(
                        userSettings.temperatureUnits
                    )
                }
                val hourly = weather.hourly.map {
                    it.convertToUserSettings(userSettings.temperatureUnits)
                }
                val newWeather = weather.copy(current = current, daily = daily, hourly = hourly)
                SavableForecastData(
                    weather = newWeather,
                    userSettings = userSettings,
                    showPlaceHolder = false
                )
            }
            .onEach { forecastData ->
                val timeStamp = forecastData.weather.current.dt
                val coordinate = Coordinate(
                    forecastData.weather.coordinates.name,
                    forecastData.weather.coordinates.lat.toString(),
                    forecastData.weather.coordinates.lon.toString()
                )
                if (isDataExpired(dataTimestamp = timeStamp, minutesThreshold = 30)) {
                    sync(coordinate)
                }
            }
            .retry(2)
            .catch {
                Timber.e("data state:${it.message}")
                Timber.e("catch2: ${dataBaseOrCityIsEmpty.value}")
            }
    }

    private fun checkDatabaseIsEmpty() {
        viewModelScope.launch {
            val isEmpty = weatherRepository.isDatabaseEmpty() == 0
            _dataBaseOrCityIsEmpty.value = isEmpty
        }
    }

    private fun getFavoriteCityCoordinate(): Flow<Coordinate?> {
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
}


sealed class WeatherUIState {
    object Loading : WeatherUIState()
    data class Success(val data: SavableForecastData) : WeatherUIState()
}
