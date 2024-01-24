package com.weather.feature.managelocations

import android.content.Context
import android.os.Vibrator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.weather.core.common.extentions.convertTotoUserSettings
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.ManageLocationsData
import com.weather.model.TemperatureUnits
import com.weather.model.TemperatureUnits.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

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
    {
            weatherList,
            favoriteCityName,
            temperatureSetting ->
        val tempUnit = temperatureSetting ?: C
        val locationData = weatherList.convertTotoUserSettings(
            tempUnit = tempUnit,
            favoriteCityName = favoriteCityName
        )
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

    fun saveFavoriteCityCoordinate(cityName: String, context: Context) {
        val hapticFeedback = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        viewModelScope.launch {
            userRepository.setFavoriteCityCoordinate(cityName)
            hapticFeedback.cancel()
            hapticFeedback.vibrate(60)
        }
    }

    @ExperimentalCoroutinesApi
    private fun getFavoriteCityCoordinate(): Flow<String?> {
        return userRepository.getFavoriteCityCoordinate()
    }

    fun deleteWeatherByCityName(cityNames: List<String>, context: Context) {
        val hapticFeedback = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteWeatherByCityName(cityName = cityNames)
            hapticFeedback.cancel()
            hapticFeedback.vibrate(60)
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

