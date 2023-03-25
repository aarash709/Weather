package com.weather.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    internal fun setTemperatureUnit(tempUnits: TemperatureUnits) {
        viewModelScope.launch {
//            repository.setTemperatureUnit
        }
    }

    internal fun setWindSpeedUnit(windSpeedUnits: WindSpeedUnits) {
        viewModelScope.launch {
//            repository.setWindSpeedUnit
        }
    }
}

internal sealed class SettingsUIState() {
    data class Success(val settingsData: SettingsData) : SettingsUIState()
    object Loading : SettingsUIState()
}

internal data class SettingsData(
    val windSpeedUnits: WindSpeedUnits,
    val temperatureUnits: TemperatureUnits,
)

enum class WindSpeedUnits {
    KM,
    MS,
    MPH,
}

enum class TemperatureUnits {
    C,
    F,
}