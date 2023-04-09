package com.weather.feature.forecast.fake

import com.weather.core.repository.UserRepository
import com.weather.model.Coordinate
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository : UserRepository {
    override fun getFavoriteCityCoordinate(): Flow<Coordinate?> = flow {
        val coordinate = listOfWeatherData.random().let {
            Coordinate(
                it.coordinates.name,
                it.coordinates.lat.toString(),
                it.coordinates.lon.toString()
            )
        }
        emit(coordinate)
    }

    override fun getTemperatureUnitSetting(): Flow<TemperatureUnits?> = flow {
        emit(TemperatureUnits.C)
    }

    override fun getWindSpeedUnitSetting(): Flow<WindSpeedUnits?> = flow {
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