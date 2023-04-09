package com.weather.feature.forecast.fake

import com.weather.core.repository.WeatherRepository
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
            emit(listOfWeatherData)
        }

    override fun isDatabaseEmpty(): Int {
        return 0
    }
}

val listOfWeatherData = listOf(
    WeatherData(
        OneCallCoordinates(
            name = "Tehran",
            lat = 10.0,
            lon = 11.0,
            timezone = "Iran/Tehran",
            timezone_offset = 123456
        ),
        Current(
            clouds = 0,
            dew_point = 0.0,
            dt = 0,
            feels_like = 0.0,
            humidity = 0,
            pressure = 0,
            sunrise = 0,
            sunset = 0,
            temp = 0.0,
            uvi = 0.0,
            visibility = 0,
            wind_deg = 0,
            wind_gust = 0.0,
            wind_speed = 0.0,
            weather = listOf()
        ),
        daily = listOf(),
        hourly = listOf()
    )
)