package com.weather.feature.forecast

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.WeatherData
import com.weather.sync.work.FetchRemoteWeatherWorker
import com.weather.sync.work.WEATHER_COORDINATE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val WEATHER_DATA_STORE = "weatherDataStore"

//val Context.dataStore by preferencesDataStore(WEATHER_DATA_STORE)

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
    private val context: Application,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val cityName = savedStateHandle.get<String>("cityName").orEmpty()

    private val _dataBaseOrCityIsEmpty = MutableStateFlow(false)
    val dataBaseOrCityIsEmpty = _dataBaseOrCityIsEmpty.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    private val workManager = WorkManager.getInstance(context)
    internal var workInfoList =
        workManager
            .getWorkInfosForUniqueWorkLiveData(
                WEATHER_FETCH_WORK_NAME
            ).asFlow()
            .map {
                it.first().state == WorkInfo.State.RUNNING
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), false)

    init {
        Timber.e("init")
        Timber.e("navigated city:$cityName")
//        getFavoriteCity()
        checkDatabase()
    }

    @ExperimentalCoroutinesApi
    val weatherUIState = getWeatherData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = WeatherUIState.Loading
    )

    @ExperimentalCoroutinesApi
    private fun getWeatherData(): Flow<WeatherUIState> {
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
            .map { weather ->
                Timber.e("invoked data stream")
                val daily = weather.daily.map {
                    it.copy(dt = unixMillisToHumanDate(it.dt.toLong(), "EEE"))
                }
                val hourly = weather.hourly.map {
                    it.copy(dt = unixMillisToHumanDate(it.dt.toLong(), "HH:mm"))
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
        }
    }

//    private fun getFavoriteCity(): Flow<String> {
//        return context.dataStore.data.map { preferences ->
//            val city = preferences[DataStoreKeys.WeatherDataStore.FAVORITE_CITY_STRING_KEY] ?: ""
//            city
//        }
//    }


    @ExperimentalCoroutinesApi
    private fun getFavoriteCityCoordinate(): Flow<Coordinate?> {
        return userRepository.getFavoriteCityCoordinate()
//        return context.dataStore.data.map { preferences ->
//            val string =
//                preferences[DataStoreKeys.WeatherDataStore.FAVORITE_CITY_COORDINATE_STRING_KEY]
//                    ?: ""
//            Timber.e(string)
//            if (string.isEmpty()) {
//                null
//            } else {
//                Json.decodeFromString<Coordinate>(string)
//            }
//        }
    }

    @Deprecated("Use new sync which is using workManager APIs")
    @ExperimentalCoroutinesApi
    fun sync() {
        viewModelScope.launch(Dispatchers.IO) {
            _isSyncing.value = true
            val coordinate = getFavoriteCityCoordinate().first()
            weatherRepository.syncWeather(coordinate!!)
            _isSyncing.value = false
        }
    }

    fun sync(savedCityCoordinate: Coordinate) {
        viewModelScope.launch {
            val coordinate = Coordinate(
                cityName = savedCityCoordinate.cityName,
                latitude = savedCityCoordinate.latitude,
                longitude = savedCityCoordinate.longitude
            )
            val stringCoordinate = Json.encodeToString(coordinate)
            val workInputData = Data.Builder()
                .putString(WEATHER_COORDINATE, stringCoordinate).build()
            val fetchDataWorkRequest =
                OneTimeWorkRequestBuilder<FetchRemoteWeatherWorker>().setInputData(workInputData)
                    .build()
            workManager.beginUniqueWork(
                WEATHER_FETCH_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                fetchDataWorkRequest
            ).enqueue()
        }
    }

    private fun unixMillisToHumanDate(unixTimeStamp: Long, pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val date = Date(unixTimeStamp * 1000) //to millisecond
        return formatter.format(date)
    }

}

object DataStoreKeys {
    object WeatherDataStore {
        val FAVORITE_CITY_STRING_KEY = stringPreferencesKey("favoriteCity")
        val FAVORITE_CITY_COORDINATE_STRING_KEY = stringPreferencesKey("favoriteCityCoordinate")
    }
}

sealed class WeatherUIState {
    object Loading : WeatherUIState()
    data class Success(val data: WeatherData) : WeatherUIState()
}

internal const val WEATHER_FETCH_WORK_NAME = "weatherSyncWorkName"