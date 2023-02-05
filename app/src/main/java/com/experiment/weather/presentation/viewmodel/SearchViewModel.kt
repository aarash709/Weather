package com.experiment.weather.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Coordinates
import com.example.model.Resource
import com.example.model.geocode.GeoSearchItem
import com.weather.core.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _searchUIState = MutableStateFlow<SearchUIState>(SearchUIState.Loading)
    val searchUIState: StateFlow<SearchUIState> = _searchUIState.asStateFlow()

    val saved = mutableStateOf(false)

    fun searchCity(cityName: String) {
        if (cityName.isEmpty()) return
        viewModelScope.launch {
            delay(500)
            weatherRepository.searchLocation(cityName)
                .map {
                    when (it) {
                        is Resource.Success -> {
                            _searchUIState.value = SearchUIState.Success(it.data!!)
                        }
                        is Resource.Loading -> {
                            _searchUIState.value = SearchUIState.Loading
                        }
                        is Resource.Error -> {
                            _searchUIState.value = SearchUIState.Error
                        }
                    }
                }.collect()
        }
    }

    fun saveSearchWeatherItem(searchItem: GeoSearchItem){
        val coordinates = Coordinates(searchItem.lat.toString(), searchItem.lon.toString())
        viewModelScope.launch{
            weatherRepository.syncLatestWeather(
                searchItem.name.toString(),
                coordinates
            )
        }
//        saved.value = dataSaved
        return
    }

}

sealed interface SearchUIState {
    data class Success(val data: List<GeoSearchItem>) : SearchUIState
    object Error : SearchUIState
    object Loading : SearchUIState
}