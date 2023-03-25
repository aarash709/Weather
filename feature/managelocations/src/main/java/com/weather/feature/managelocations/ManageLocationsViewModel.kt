package com.weather.feature.managelocations

import android.app.Application
import android.content.Context
import android.os.Vibrator
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ManageLocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
    private val context: Application,
) : ViewModel() {

    private val hapticFeedback = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationsState = weatherRepository.getAllWeatherLocations()
        .combine(getFavoriteCityCoordinate()) { weatherList, favoriteCoordinate ->
            val locationData = weatherList.map {
                val isFavorite = it.locationName == favoriteCoordinate?.cityName
                it.copy(isFavorite = isFavorite)
            }
            LocationsUIState.Success(locationData)
        }.catch {
            Timber.e(it.message)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LocationsUIState.Loading
        )

    @Deprecated("use new method")
    fun saveFavoriteCity(cityName: String) {
        if (cityName.isEmpty()) {
            Timber.e("city name is empty")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
//            context.dataStore.edit { preference ->
//                preference[DataStoreKeys.WeatherDataStore.FAVORITE_CITY_String_Key] = cityName
//            }
        }
    }

    fun saveFavoriteCityCoordinate(coordinate: Coordinate) {
        viewModelScope.launch {
            val coordinateString = Json.encodeToString(coordinate)
            userRepository.setFavoriteCityCoordinate(coordinateString)
//            context.dataStore.edit { preference ->
//                preference[DataStoreKeys.WeatherDataStore.FAVORITE_CITY_Coordinate_String_Key] =
//                    coordinateString
//            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getFavoriteCityCoordinate(): Flow<Coordinate?> {
        return userRepository.getFavoriteCityCoordinate()
//        return context.dataStore.data.map { preferences ->
//            val coordinateString =
//                preferences[DataStoreKeys.WeatherDataStore.FAVORITE_CITY_Coordinate_String_Key]
//                    ?: ""
//            Timber.e(coordinateString)
//            if (coordinateString.isEmpty()) {
//                null
//            } else {
//                Json.decodeFromString<Coordinate>(coordinateString)
//            }
//        }
    }

    fun deleteWeatherByCityName(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteWeatherByCityName(cityName = cityName)
        }
        hapticFeedback.cancel()
        hapticFeedback.vibrate(10)

    }

}

sealed interface LocationsUIState {
    object Loading : LocationsUIState
    data class Success(val data: List<ManageLocationsData>) : LocationsUIState
}

object DataStoreKeys {
    object WeatherDataStore {
        val FAVORITE_CITY_String_Key = stringPreferencesKey("favoriteCity")
        val FAVORITE_CITY_Coordinate_String_Key = stringPreferencesKey("favoriteCityCoordinate")
    }
}

