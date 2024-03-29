package com.weather.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.UserRepository
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.TemperatureUnits.*
import com.weather.model.WindSpeedUnits
import com.weather.model.WindSpeedUnits.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    internal val settingsUIState = repository.getTemperatureUnitSetting()
        .combine(repository.getWindSpeedUnitSetting()) { temperatureUnit, windSpeedUnit ->
            val tempUnit = temperatureUnit ?: C
            val windUnit = windSpeedUnit ?: KM
            val settingData =
                SettingsData(windSpeedUnits = windUnit, temperatureUnits = tempUnit)
            Timber.e(settingData.toString())
            SettingsUIState.Success(settingsData = settingData)
        }.catch {
            Timber.e(it.message)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000),
            SettingsUIState.Loading
        )

    internal fun setTemperatureUnit(tempUnit: TemperatureUnits) {
        viewModelScope.launch {
            Timber.e(tempUnit.toString())
            repository.setTemperatureUnitSetting(tempUnit)
        }
    }

    internal fun setWindSpeedUnit(windSpeedUnits: WindSpeedUnits) {
        viewModelScope.launch {
            Timber.e(windSpeedUnits.toString())
            repository.setWindSpeedUnitSetting(windSpeedUnits)
        }
    }
}

sealed class SettingsUIState {
    data class Success(val settingsData: SettingsData) : SettingsUIState()
    object Loading : SettingsUIState()
}
