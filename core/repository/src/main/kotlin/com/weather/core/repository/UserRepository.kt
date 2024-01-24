package com.weather.core.repository

import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getFavoriteCityCoordinate(): Flow<String?>

    fun getTemperatureUnitSetting(): Flow<TemperatureUnits?>

    fun getWindSpeedUnitSetting(): Flow<WindSpeedUnits?>

    suspend fun setFavoriteCityCoordinate(value: String)

    suspend fun setTemperatureUnitSetting(tempUnit: TemperatureUnits)

    suspend fun setWindSpeedUnitSetting(windSpeedUnit: WindSpeedUnits)

}