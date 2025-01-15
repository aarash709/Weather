package com.weather.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.weather.core.common.extentions.convertTimeAndTemperature
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.core.repository.data.getWeatherCondition
import com.weather.core.repository.data.weatherCode
import com.weather.model.Coordinate
import com.weather.model.DailyPreview
import com.weather.model.geocode.GeoSearchItem
import com.weather.model.geocode.SavableSearchState
import com.weather.sync.work.utils.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

	private val _weatherPreview = MutableStateFlow<List<DailyPreview>>(listOf())
	val weatherPreview: StateFlow<List<DailyPreview>> =
		_weatherPreview.asStateFlow() // use to show a preview to the user while searching

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
		.map { geoItemList ->
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
			val isDatabaseEmpty = weatherRepository.isDatabaseEmpty()

			if (isDatabaseEmpty) {
				userRepository.setFavoriteCityCoordinate(stringCoordinate)
				Timber.e(stringCoordinate)
			}
			//work
			syncStatus.syncWithCoordinate(coordinate)
		}
	}

	fun getFiveDayPreview(geoSearchItem: GeoSearchItem) {
		viewModelScope.launch {
			val data = weatherRepository.getFiveDay(
				coordinate = Coordinate(
					geoSearchItem.name,
					geoSearchItem.lat.toString(),
					geoSearchItem.lon.toString()
				)
			).map {
				val condition = getWeatherCondition(wmoCode = it.weatherCode.toString(), isDay = 1)
				it.copy(iconUrl = condition.image)
			}.convertTimeAndTemperature()
			_weatherPreview.tryEmit(data)
		}
	}
}