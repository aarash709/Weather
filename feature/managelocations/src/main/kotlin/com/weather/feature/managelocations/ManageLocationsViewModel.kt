package com.weather.feature.managelocations

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.weather.core.common.extentions.convertTotoUserSettings
import com.weather.core.repository.UserRepository
import com.weather.core.repository.WeatherRepository
import com.weather.model.ManageLocationsData
import com.weather.model.TemperatureUnits.C
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ManageLocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationsState = combine(
        weatherRepository.getAllWeatherLocations(),
        getFavoriteCityCoordinate(),
        userRepository.getTemperatureUnitSetting()
    ) {
            weatherList,
            favoriteCityName,
            temperatureSetting,
        ->
        val tempUnit = temperatureSetting ?: C
        val locationData = weatherList.convertTotoUserSettings(
            tempUnit = tempUnit,
            favoriteCityName = favoriteCityName
        )
        LocationsUIState.Success(locationData)
    }
        .catch {
            Timber.e(it.message)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LocationsUIState.Loading
        )


    fun saveFavoriteCityCoordinate(cityName: String) {
        viewModelScope.launch {
            userRepository.setFavoriteCityCoordinate(cityName)
        }
    }

    private fun getFavoriteCityCoordinate(): Flow<String?> {
        return userRepository.getFavoriteCityCoordinate()
    }

    fun deleteWeatherByCityName(cityNames: List<String>, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteWeatherByCityName(cityNames = cityNames)
            vibrate(context = context)
        }
    }

    private fun vibrate(context: Context) {
        val vibrationEffect = VibrationEffect.createOneShot(75, 50)
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.cancel()
        vibrator.vibrate(vibrationEffect)
    }
}

sealed interface LocationsUIState {
    data object Loading : LocationsUIState
    data class Success(val data: List<ManageLocationsData>) : LocationsUIState
}

