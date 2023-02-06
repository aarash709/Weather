package com.experiment.weather.presentation.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.WeatherRepository
import com.weather.feature.forecast.DataStoreKeys
import com.weather.feature.forecast.dataStore
import com.weather.model.ManageLocationsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ManageLocationsViewModel @Inject constructor(
    weatherRepository: WeatherRepository,
    private val context: Application,
) : ViewModel() {

    val locationsState = weatherRepository.getAllLocationsWeatherData().map {
        LocationsUIState.Success(it)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LocationsUIState.Loading
    )

    fun saveFavoriteCity(cityName: String) {
        if (cityName.isEmpty()) {
            Timber.e("city name is empty")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            context.dataStore.edit { preference ->
                preference[DataStoreKeys.FAVORITE_CITY] = cityName
            }
        }
    }

    fun deleteLocalWeatherByName(cityName: String) {

    }

}

sealed interface LocationsUIState {
    object Loading : LocationsUIState
    data class Success(val data: List<ManageLocationsData>) : LocationsUIState
}


