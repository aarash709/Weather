package com.experiment.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.network.util.NetworkManager
import com.weather.core.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    networkManager: NetworkManager,
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    val hasInternet: StateFlow<Boolean> = networkManager.hasInternet.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1_000),
        initialValue = false
    )

    private val _dataBaseIsEmpty = MutableStateFlow(false)
    val dataBaseIsEmpty = _dataBaseIsEmpty.asStateFlow()

    val isDataLoaded = weatherRepository.getAllForecastWeatherData()
        .map {
            it.isNotEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1_000),
            initialValue = false
        )

    init {
        checkDatabaseIsEmpty()
    }

    private fun checkDatabaseIsEmpty() {
        viewModelScope.launch {
            val isEmpty = weatherRepository.isDatabaseEmpty()
            _dataBaseIsEmpty.value = isEmpty
        }
    }
}

