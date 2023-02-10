package com.weather.feature.forecast

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.WeatherRepository
import com.weather.model.WeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

const val FavoriteLocation = "FavoriteLocation"
const val WEATHER_DATA_STORE = "weatherDataStore"

val Context.dataStore by preferencesDataStore(WEATHER_DATA_STORE)

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val context: Application,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val cityName = savedStateHandle.get<String>("cityName").orEmpty()

    private val _dataBaseOrCityIsEmpty = MutableStateFlow(false)
    val dataBaseOrCityIsEmpty = _dataBaseOrCityIsEmpty.asStateFlow()

    init {
        Timber.e("init")
        Timber.e("navigated city:$cityName")
        getFavoriteCity()
        checkDatabase()
    }

    val weatherUIState = getWeatherData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = WeatherUIState.Loading
    )

    private fun getWeatherData(): Flow<WeatherUIState> {
        return weatherRepository.getAllForecastWeatherData()
            .zip(getFavoriteCity()) { allWeather, cityName ->
                Timber.e("cityName: $cityName")
                if (cityName.isBlank())
                    allWeather.first()
                else
                    allWeather.first { it.coordinates.name == cityName }
            }
            .flowOn(Dispatchers.IO)
            .map { weather ->
                Timber.e("invoked data stream")
                val daily = weather.daily.map {
                    it.copy(dt = unixToDate(it.dt.toLong(),"EEE"))
                }
                val hourly = weather.hourly.map {
                    it.copy(dt = unixToDate(it.dt.toLong(),"HH:mm"))
                }
                val newWeather = weather.copy(daily = daily, hourly = hourly)
                WeatherUIState.Success(newWeather)
            }
            .retry(2)
            .catch {
                Timber.e("data state:${it.message}")
                Timber.e("catch2: ${dataBaseOrCityIsEmpty.value}")
                WeatherUIState.Loading
            }
    }

    private fun checkDatabase() {
        viewModelScope.launch {
            val isEmpty = weatherRepository.isDatabaseEmpty() == 0
            _dataBaseOrCityIsEmpty.value = isEmpty
            Timber.e("vm database method count: ${weatherRepository.isDatabaseEmpty()}")
            Timber.e("vm database is empty: $isEmpty")
        }
    }

    private fun getFavoriteCity(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            val city = preferences[DataStoreKeys.FAVORITE_CITY] ?: ""
            Timber.e("store city is: $city")
            city
        }

    }
//    suspend fun sync(cityName: String, coords: Coordinates) {
//        weatherRepository.syncLatestWeather(cityName = cityName, coords)
//    }

    private fun unixToDate(unixTimeStamp: Long,pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val date = Date(unixTimeStamp * 1000)
        return formatter.format(date)
    }

}

object DataStoreKeys {
    val FAVORITE_CITY = stringPreferencesKey("favoriteCity")
}

sealed class WeatherUIState {
    object Loading : WeatherUIState()
    data class Success(val data: WeatherData) : WeatherUIState()
}