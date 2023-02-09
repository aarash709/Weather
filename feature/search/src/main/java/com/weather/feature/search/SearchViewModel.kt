package com.weather.feature.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinates
import com.weather.model.Resource
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
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

    private val _weatherPreview = MutableSharedFlow<WeatherData>()
    val weatherPreview: SharedFlow<WeatherData> = _weatherPreview.asSharedFlow()

    val saved = mutableStateOf(false)

    @FlowPreview
    fun searchCity(cityName: String) {
        if (cityName.isEmpty()) return
        viewModelScope.launch {
            weatherRepository.searchLocation(cityName)
                .debounce(500L)
                .map {search->
                    when (search) {
                        is Resource.Success -> {
                            _searchUIState.value = SearchUIState.Success(search.data!!)
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
            weatherRepository.syncWeather(
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