package com.weather.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    internal fun setTemperatureUnit(tempUnit: TemperatureUnits) {
        viewModelScope.launch {
            repository.setTemperatureUnitSetting(tempUnit)
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
