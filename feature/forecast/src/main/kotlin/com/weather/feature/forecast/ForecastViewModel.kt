package com.weather.feature.forecast

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.weather.core.common.extentions.convertToUserSettings
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.Hourly
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.Locale
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


    @ExperimentalCoroutinesApi
    internal val weatherUIState = getWeatherData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = SavableForecastData.placeholderDefault
    )

    private fun getWeatherData(): Flow<SavableForecastData> {
        return weatherRepository.getAllForecastWeatherData()
            .combine(getFavoriteCityCoordinate()) { allWeather, favoriteCityName ->
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
                val hourly = newWeather.hourly
                val current = newWeather.current
                val hourlyData = calculateSunriseAndSunset(
                    hourlyData = hourly,
                    sunrise = current.sunrise,
                    sunset = current.sunset
                )
                SavableForecastData(
                    weather = newWeather.copy(hourly = hourlyData),
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

    private fun calculateSunriseAndSunset(
        hourlyData: List<Hourly>,
        sunrise: Int,
        sunset: Int,
    ): List<Hourly> {
        val mutableHourly = hourlyData.toMutableList()
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedSunset = formatter.format(Date(sunset.toLong() * 1000))
        val formattedSunrise = formatter.format(Date(sunrise.toLong() * 1000))
        hourlyData.lastOrNull { it.dt <= sunset }
            ?.let { hourly ->
                val index = hourlyData.indexOf(hourly)
                val sunsetHourly = hourly.copy(
                    sunriseSunset = "Sunset",
                    time = formattedSunset,
                    dt = sunset
                )
                mutableHourly.add(index, sunsetHourly)
            }
        hourlyData.lastOrNull { it.dt <= sunrise }
            ?.let { hourly ->
                val index = hourlyData.indexOf(hourly)
                val sunsetHourly = hourly.copy(
                    sunriseSunset = "Sunrise",
                    time = formattedSunrise,
                    dt = sunrise
                )
                mutableHourly.add(index, sunsetHourly)
            }
        return mutableHourly.toList()
    }

    private fun weatherBackgroundDrawableRes(conditionID: Int, timeOfDay: TimeOfDay): Int {
        //more details -> https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
        return when (conditionID) {
            800 -> {
                when (timeOfDay) {
                    TimeOfDay.Day -> R.drawable.day_clear
                    TimeOfDay.Night -> R.drawable.night_clear
                    TimeOfDay.Dawn -> R.drawable.dawn
                }
            }
            in 200..232 -> R.drawable.thunderstorm //Thunderstorm
            in 300..321 -> R.drawable.thunderstorm //Drizzle
            in 500..531 -> R.drawable.snow //Rain
            in 600..622 -> R.drawable.snow //Snow
            in 701..787 -> R.drawable.fog //Atmosphere(only fog is shown. will add more)
            in 801..804 -> R.drawable.clouds //clouds
            else -> R.drawable.day_clear
        }
    }
}