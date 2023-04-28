package com.weather.feature.forecast

import androidx.lifecycle.*
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.*
import com.weather.model.TemperatureUnits.*
import com.weather.model.WindSpeedUnits.*
import com.weather.sync.work.utils.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.*
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

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
        Timber.e("init")
        Timber.e("navigated city:$cityName")
        checkDatabase()
    }

    @ExperimentalCoroutinesApi
    internal fun getWeatherData(): Flow<SavableForecastData> {
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
                Timber.e("invoked data stream")
                Timber.e("user settings:  ${userSettings.temperatureUnits}")
                val current = weather.current.run {
                    copy(
                        dew_point = dew_point.convertToUserTemperature(
                            userSettings.temperatureUnits ?: C
                        ),
                        feels_like = feels_like.convertToUserTemperature(
                            userSettings.temperatureUnits ?: C
                        ),
                        temp = temp.convertToUserTemperature(
                            userSettings.temperatureUnits ?: C
                        ),
                        visibility = visibility,
                        wind_speed = wind_speed.convertToUserSpeed(
                            userSettings.windSpeedUnits ?: KM
                        ),
                    )
                }
                val daily = weather.daily.map {
                    it.copy(
                        dew_point = it.dew_point.convertToUserTemperature(
                            userSettings.temperatureUnits ?: C
                        ),
                        dt = unixMillisToHumanDate(it.dt.toLong(), "EEE"),
                        dayTemp = it.dayTemp.convertToUserTemperature(

                            userSettings.temperatureUnits ?: C
                        ),
                        nightTemp = it.nightTemp.convertToUserTemperature(
                            userSettings.temperatureUnits ?: C
                        )
                    )
                }
                val hourly = weather.hourly.map {
                    it.copy(
                        dew_point = it.dew_point.convertToUserTemperature(
                            userSettings.temperatureUnits ?: C
                        ),
                        dt = unixMillisToHumanDate(it.dt.toLong(), "HH:mm"),
                        temp = it.temp.convertToUserTemperature(
                            userSettings.temperatureUnits ?: C
                        )
                    )
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

    private fun checkDatabase() {
        viewModelScope.launch {
            val isEmpty = weatherRepository.isDatabaseEmpty() == 0
            _dataBaseOrCityIsEmpty.value = isEmpty
        }
    }

    @ExperimentalCoroutinesApi
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

    private fun unixMillisToHumanDate(unixTimeStamp: Long, pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val date = Date(unixTimeStamp * 1000) //to millisecond
        return formatter.format(date)
    }

    internal fun isDataExpired(dataTimestamp: Int, minutesThreshold: Int): Boolean {
        val currentTime = Instant.now().epochSecond
        val differanceInSeconds = currentTime.minus(dataTimestamp)
        val differanceInMinutes = Duration.ofSeconds(differanceInSeconds).toMinutes()
        Timber.e((differanceInMinutes > minutesThreshold).toString())
        return differanceInMinutes > minutesThreshold
    }

    internal fun Double.convertToUserTemperature(
        userTempUnit: TemperatureUnits,
    ): Double {
        return when (userTempUnit) {
            C -> this.minus(273.15)
            F -> this.minus(273.15).times(1.8f).plus(32)
        }
    }

    internal fun Double.convertToUserSpeed(
        userTempUnit: WindSpeedUnits,
    ): Double {
        return when (userTempUnit) {
            KM -> this.times(3.6f).times(100).roundToInt().toDouble().div(100)
            MS -> this
            MPH -> this.times(2.2369f).times(100).roundToInt().toDouble().div(100)
        }
    }

    internal fun Int.compactVisibilityMeasurement(): Int {
        return when {
            this < 1000 -> {
                return this
            }

            this > 1000 -> {
                return this.div(1000)
            }

            else -> {
                this
            }
        }
    }
}


sealed class WeatherUIState {
    object Loading : WeatherUIState()
    data class Success(val data: SavableForecastData) : WeatherUIState()
}
