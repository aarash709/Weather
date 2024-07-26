package com.weather.core.repository.fake

import com.weather.core.repository.UserRepository
import com.weather.core.repository.fake.data.listOfWeatherDataTest
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository : UserRepository {
    override fun getFavoriteCityCoordinate(): Flow<String?> = flow {
        val cityName = listOfWeatherDataTest.random().coordinates.name
        emit(cityName)
    }

    override fun getTemperatureUnitSetting(): Flow<TemperatureUnits> = flow {
        emit(TemperatureUnits.C)
    }

    override fun getWindSpeedUnitSetting(): Flow<WindSpeedUnits> = flow {
        emit(WindSpeedUnits.KM)
    }

    override suspend fun setFavoriteCityCoordinate(value: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setTemperatureUnitSetting(tempUnit: TemperatureUnits) {
        TODO("Not yet implemented")
    }

    override suspend fun setWindSpeedUnitSetting(windSpeedUnit: WindSpeedUnits) {
        TODO("Not yet implemented")
    }
}