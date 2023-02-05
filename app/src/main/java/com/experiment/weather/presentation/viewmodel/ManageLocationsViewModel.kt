package com.experiment.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.WeatherRepository
import com.weather.model.ManageLocationsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ManageLocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _weatherDataList = MutableStateFlow<LocationsUIState>(LocationsUIState.Loading)
    val weatherDataList: StateFlow<LocationsUIState> = _weatherDataList.asStateFlow()

    val state = weatherRepository.getAllLocationsWeatherData().map {
        LocationsUIState.Success(it)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LocationsUIState.Loading
    )

    fun deleteLocalWeatherByName(cityName: String) {

    }

}

sealed interface LocationsUIState {
    object Loading : LocationsUIState
    data class Success(val data: List<ManageLocationsData>) : LocationsUIState
}


