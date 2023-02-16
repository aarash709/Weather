package com.weather.feature.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.Resource
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _weatherPreview = MutableSharedFlow<WeatherData>()
    val weatherPreview: SharedFlow<WeatherData> = _weatherPreview.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    private val searchQuery = _searchQuery.asStateFlow()

    @ExperimentalCoroutinesApi
    @FlowPreview
    val searchUIState = searchQuery
        .debounce(500L)
        .filterNot {
            it.isBlank()
        }
        .flatMapLatest { cityName ->
            weatherRepository.searchLocation(cityName = cityName)
                .flowOn(Dispatchers.IO)
                .map { search ->
                    when (search) {
                        is Resource.Success -> {
                            SearchUIState.Success(search.data!!)
                        }
                        is Resource.Loading -> {
                            SearchUIState.Loading
                        }
                        is Resource.Error -> {
                            SearchUIState.Error
                        }
                    }
                }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = SearchUIState.Loading
        )

    fun setSearchQuery(cityName: String) {
        _searchQuery.value = cityName
    }

    fun saveSearchWeatherItem(searchItem: GeoSearchItem) {
        val coordinates =
            Coordinate(searchItem.name, searchItem.lat.toString(), searchItem.lon.toString())
        viewModelScope.launch {
            weatherRepository.syncWeather(
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