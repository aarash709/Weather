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
import com.weather.model.TemperatureUnits
import com.weather.model.TemperatureUnits.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ManageLocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationsState = combine(
        weatherRepository.getAllWeatherLocations(),
        getFavoriteCityCoordinate(),
        userRepository.getTemperatureUnitSetting()
    )
    { weatherList, favoriteCoordinate, temperatureSetting ->
        val tempUnit = temperatureSetting ?: C
        val locationData = weatherList.map {
            val isFavorite = it.locationName == favoriteCoordinate?.cityName
            it.copy(
                currentTemp = it.currentTemp.toDouble()
                    .convertToUserTemperature(userTempUnit = tempUnit).roundToInt().toString(),
                feelsLike = it.feelsLike.toDouble()
                    .convertToUserTemperature(userTempUnit = tempUnit).roundToInt().toString(),
                isFavorite = isFavorite
            )
        }
        LocationsUIState.Success(locationData)
    }
        .catch {
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

    fun deleteWeatherByCityName(cityName: String, context: Context) {
        val hapticFeedback = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteWeatherByCityName(cityName = cityName)
            hapticFeedback.cancel()
            hapticFeedback.vibrate(20)
        }

    }

    internal fun Double.convertToUserTemperature(
        userTempUnit: TemperatureUnits,
    ): Double {
        return when (userTempUnit) {
            C -> this.minus(273.15)
            F -> this.minus(273.15).times(1.8f).plus(32)
        }
    }

}

sealed interface LocationsUIState {
    object Loading : LocationsUIState
    data class Success(val data: List<ManageLocationsData>) : LocationsUIState
}

