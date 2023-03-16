package com.experiment.weather.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.WeatherRepository
import com.weather.feature.forecast.DataStoreKeys
import com.weather.feature.forecast.dataStore
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ManageLocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val context: Application,
) : ViewModel() {

    private val hapticFeedback = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationsState = weatherRepository.getAllWeatherLocations()
        .combine(getFavoriteCityCoordinate()) { weatherList, favoriteCoordinate ->
            val locationData = weatherList.map {
                val isFavorite = it.locationName == favoriteCoordinate.cityName
                it.copy(isFavorite = isFavorite)
            }
            LocationsUIState.Success(locationData)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LocationsUIState.Loading
        )

    @Deprecated("use new method")
    fun saveFavoriteCity(cityName: String) {
        if (cityName.isEmpty()) {
            Timber.e("city name is empty")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            context.dataStore.edit { preference ->
                preference[DataStoreKeys.WeatherDataStore.FAVORITE_CITY_String_Key] = cityName
            }
        }
    }

    fun saveFavoriteCityCoordinate(coordinate: Coordinate) {
        viewModelScope.launch(Dispatchers.IO) {
            val coordinateString = Json.encodeToString(coordinate)
            context.dataStore.edit { preference ->
                preference[DataStoreKeys.WeatherDataStore.FAVORITE_CITY_Coordinate_String_Key] =
                    coordinateString
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getFavoriteCityCoordinate(): Flow<Coordinate> {
        return context.dataStore.data.map { preferences ->
            val string =
                preferences[DataStoreKeys.WeatherDataStore.FAVORITE_CITY_Coordinate_String_Key]
                    ?: ""
            Timber.e(string)
            string
        }.map { coordinate ->
            Json.decodeFromString<Coordinate>(coordinate)
        }
    }

    fun deleteWeatherByCityName(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteWeatherByCityName(cityName = cityName)
        }
        hapticFeedback.cancel()
        hapticFeedback.vibrate(10)

    }

}

sealed interface LocationsUIState {
    object Loading : LocationsUIState
    data class Success(val data: List<ManageLocationsData>) : LocationsUIState
}


