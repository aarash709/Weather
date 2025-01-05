package com.weather.core.repository.fake

import com.weather.core.repository.WeatherRepository
import com.weather.core.repository.fake.data.listOfLocationsDataTest
import com.weather.core.repository.fake.data.listOfWeatherDataTest
import com.weather.model.*
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository : WeatherRepository {
//    override suspend fun getFiveDay(coordinate: Coordinate): List<DailyPreview> {
//        TODO("Not yet implemented")
//    }

    override fun searchLocation(cityName: String): Flow<List<GeoSearchItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrent(latitude: String, longitude: String, params: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getDaily(latitude: String, longitude: String, params: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getHourly(latitude: String, longitude: String, params: String) {
        TODO("Not yet implemented")
    }

    override suspend fun syncWeather(cityName: String, coordinate: Coordinate) {
        TODO("Not yet implemented")
    }

    override suspend fun syncWeather(coordinate: Coordinate) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherByCityName(cityNames: List<String>) {
        TODO("Not yet implemented")
    }

    override fun getLocalWeatherByCityName(cityName: String): Flow<WeatherData> = flow {
        TODO("Not yet implemented")
    }

    override fun getAllWeatherLocations(): Flow<List<ManageLocationsData>> =
        flow {
            emit(listOfLocationsDataTest)
        }

    override suspend fun reorderData(locations: List<ManageLocationsData>) {
        TODO("Not yet implemented")
    }


    override fun getAllForecastWeatherData(): Flow<List<WeatherData>> =
        flow {
            emit(listOfWeatherDataTest)
        }

    override fun isDatabaseEmpty(): Boolean {
        return true
    }
}