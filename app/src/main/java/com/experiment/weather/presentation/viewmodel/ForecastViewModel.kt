package com.experiment.weather.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.weather.data.remote.model.weatherData.WeatherData
import com.experiment.weather.presentation.viewmodel.WeatherUIState.Success
import com.experiment.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

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
        Timber.e(cityName)
        saveFavoriteCity(cityName)
        checkDatabase()
        Timber.e("init")
        Timber.e("city:$cityName")

        val a = context.dataStore.data.catch { }.map { preferences ->
            preferences[DataStoreKeys.FAVORITE_CITY] ?: "no value"
        }
    }

    val weatherUIState = getWeatherData()

    private fun getWeatherData(): StateFlow<WeatherUIState> {
        return weatherRepository
            .weatherLocalDataStream(cityName = getFavoriteCity()).mapNotNull {
                Timber.e("invoked data stream")
//                if (dataBaseOrCityIsEmpty.value)
//                    WeatherUIState.Loading
                Success(it)
            }
            .flowOn(Dispatchers.IO)
            .catch {
                Timber.e("data state:${it.message}")
                Timber.e("catch2: ${dataBaseOrCityIsEmpty.value}")
                WeatherUIState.Loading
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(1000),
                initialValue = WeatherUIState.Loading
            )
    }

    private fun checkDatabase() {
        viewModelScope.launch {
            val isEmpty = weatherRepository.databaseIsEmpty() == 0
            _dataBaseOrCityIsEmpty.value = isEmpty || getFavoriteCity().isEmpty()
            Timber.e("vm database method count: ${weatherRepository.databaseIsEmpty()}")
            Timber.e("vm database is empty: $isEmpty")
            Timber.e("vm saved city is empty: ${getFavoriteCity().isEmpty()}")
        }
    }

    private fun saveFavoriteCity(cityName: String) {
        if (cityName.isEmpty()) {
            Timber.e("city name is empty")
            return
        }
        viewModelScope.launch {
            context.dataStore.edit { preference ->
                preference[DataStoreKeys.FAVORITE_CITY] = cityName
            }
        }
    }

    private fun getFavoriteCity(): String {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                val city = preferences[DataStoreKeys.FAVORITE_CITY] ?: ""
                Timber.e("store city is: $city")
                city
            }.first()
        }
    }
//    fun getWeather(cityName: String): Flow<WeatherUIState> {
//        return weatherRepository.getLocalWeather(cityName = cityName).map {
//            Timber.e(it.coordinates.timezone)
//            Success(it)
//        }
//    }
//
//    suspend fun sync(cityName: String, coords: Coordinates) {
//        weatherRepository.syncLatestWeather(cityName = cityName, coords)
//    }
}

object DataStoreKeys {
    val FAVORITE_CITY = stringPreferencesKey("favoriteCity")
}

sealed class WeatherUIState {
    object Loading : WeatherUIState()
    data class Success(val data: WeatherData) : WeatherUIState()
}