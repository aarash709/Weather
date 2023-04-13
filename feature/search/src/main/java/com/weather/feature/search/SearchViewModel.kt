package com.weather.feature.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.Resource
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import com.weather.sync.work.FetchRemoteWeatherWorker
import com.weather.sync.work.WEATHER_COORDINATE
import com.weather.sync.work.WorkSyncStatus
import com.weather.sync.work.utils.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val syncStatus: SyncManager,
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
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

    fun syncWeather(searchItem: GeoSearchItem) {
        viewModelScope.launch {
            val coordinate = Coordinate(
                cityName = searchItem.name,
                latitude = searchItem.lat.toString(),
                longitude = searchItem.lon.toString()
            )

            val stringCoordinate = Json.encodeToString(coordinate)
            val isDatabaseEmpty = weatherRepository.isDatabaseEmpty() == 0

            if (isDatabaseEmpty) {
                userRepository.setFavoriteCityCoordinate(stringCoordinate)
                Timber.e(stringCoordinate)
            }
            //work
            syncStatus.syncWithCoordinate(coordinate)
        }

        fun saveSearchWeatherItem(searchItem: GeoSearchItem) {
            val coordinates = Coordinate(
                searchItem.name,
                searchItem.lat.toString(),
                searchItem.lon.toString()
            )
            viewModelScope.launch {
                weatherRepository.syncWeather(
                    coordinates
                )
            }
        }
    }
}

sealed interface SearchUIState {
    data class Success(val data: List<GeoSearchItem>) : SearchUIState
    object Error : SearchUIState
    object Loading : SearchUIState
}