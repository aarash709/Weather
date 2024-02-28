package com.weather.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.WeatherData
import com.weather.model.geocode.GeoSearchItem
import com.weather.model.geocode.SavableSearchState
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
    val weatherPreview: SharedFlow<WeatherData> = _weatherPreview.asSharedFlow() // use to show a preview to the user while searching

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
        }
        .flowOn(Dispatchers.IO)
        .map { geoItemList->
            SavableSearchState(
                geoSearchItems = geoItemList,
                showPlaceholder = false
            )
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = SavableSearchState.empty
        )

    internal fun setSearchQuery(cityName: String) {
        _searchQuery.value = cityName
    }

    internal fun syncWeather(searchItem: GeoSearchItem) {
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
    }
}