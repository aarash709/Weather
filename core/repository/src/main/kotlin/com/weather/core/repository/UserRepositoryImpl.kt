package com.weather.core.repository

import com.experiment.weather.core.common.extentions.Dispachers
import com.experiment.weather.core.common.extentions.WeatherDidpatchers
import com.weather.core.datastore.LocalUserPreferences
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userPreferences: LocalUserPreferences,
    @Dispachers(WeatherDidpatchers.IO) private val IODispatcher: CoroutineDispatcher
) : UserRepository {
    override fun getFavoriteCityCoordinate(): Flow<String?> {
        return userPreferences.getFavoriteCity()
    }

    override fun getTemperatureUnitSetting(): Flow<TemperatureUnits?> {
        return userPreferences.getTemperatureUnitSetting().map { stringTemp ->
            if (stringTemp != null) {
                Json.decodeFromString<TemperatureUnits>(stringTemp)
            } else {
                null
            }
        }
    }

    override fun getWindSpeedUnitSetting(): Flow<WindSpeedUnits?> {
        return userPreferences.getWindSpeedUnitSetting().map { windSpeedString ->
            if (windSpeedString != null) {
                Json.decodeFromString<WindSpeedUnits>(windSpeedString)
            } else {
                null
            }
        }
    }

    override suspend fun setFavoriteCityCoordinate(value: String) {
        withContext(IODispatcher) {
            userPreferences.setFavoriteCity(value)
        }
    }

    override suspend fun setTemperatureUnitSetting(tempUnit: TemperatureUnits) {
        withContext(IODispatcher) {
            val stringTempUnit = Json.encodeToString(tempUnit)
            userPreferences.setTemperatureUnit(stringTempUnit)
        }
    }

    override suspend fun setWindSpeedUnitSetting(windSpeedUnit: WindSpeedUnits) {
        withContext(IODispatcher) {
            val stringWindSpeedUnit = Json.encodeToString(windSpeedUnit)
            userPreferences.setWindSpeedUnit(stringWindSpeedUnit)
        }
    }

}