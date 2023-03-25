package com.weather.feature.managelocations

import android.app.Application
import android.content.Context
import android.os.Vibrator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ManageLocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
    private val context: Application,
) : ViewModel() {

    private val hapticFeedback = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationsState = weatherRepository.getAllWeatherLocations()
        .combine(getFavoriteCityCoordinate()) { weatherList, favoriteCoordinate ->
            val locationData = weatherList.map {
                val isFavorite = it.locationName == favoriteCoordinate?.cityName
                it.copy(isFavorite = isFavorite)
            }
            LocationsUIState.Success(locationData)
        }.catch {
            Timber.e(it.message)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LocationsUIState.Loading
        )

    fun saveFavoriteCityCoordinate(coordinate: Coordinate) {
        viewModelScope.launch {
            val coordinateString = Json.encodeToString(coordinate)
            userRepository.setFavoriteCityCoordinate(coordinateString)
        }
    }

    @ExperimentalCoroutinesApi
    private fun getFavoriteCityCoordinate(): Flow<Coordinate?> {
        return userRepository.getFavoriteCityCoordinate()
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

