package com.weather.core.repository.fake

import com.weather.core.repository.WeatherRepository
import com.weather.core.repository.fake.data.listOfWeatherDataTest
import com.weather.model.*
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository : WeatherRepository {

    override fun searchLocation(cityName: String): Flow<Resource<List<GeoSearchItem>>> {
        TODO("Not yet implemented")
    }

    override suspend fun syncWeather(cityName: String, coordinate: Coordinate) {
        TODO("Not yet implemented")
    }

    override suspend fun syncWeather(coordinate: Coordinate) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherByCityName(cityName: String) {
        TODO("Not yet implemented")
    }

    override fun getLocalWeatherByCityName(cityName: String): Flow<WeatherData> = flow {
        TODO("Not yet implemented")
    }

    override fun getAllWeatherLocations(): Flow<List<ManageLocationsData>> {
        TODO("Not yet implemented")
    }

    override fun getAllForecastWeatherData(): Flow<List<WeatherData>> =
        flow {
            emit(listOfWeatherDataTest)
        }

    override fun isDatabaseEmpty(): Int {
        return 0
    }
}