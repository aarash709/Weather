package com.weather.feature.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    internal fun setTemperatureUnit(tempUnits: TempUnits) {
    }

    internal fun  setWindSpeedUnit(windSpeedUnits: WindSpeedUnits){
    }
}
enum class WindSpeedUnits{
    KM,
    MS,
    MPH,
}
enum class TempUnits{
    C,
    F,
}