package com.weather

import com.weather.core.database.WeatherDao
import com.weather.entities.onecall.CurrentEntity
import com.weather.entities.onecall.DailyEntity
import com.weather.entities.onecall.OneCallEntity
import com.weather.entities.onecall.CurrentWeatherEntity
import com.weather.entities.relation.OneCallAndCurrent
import com.weather.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class WeatherLocalDataSource(
    private val dao: WeatherDao,
) {

    fun getAllLocalWeatherData(): Flow<List<OneCallAndCurrent>> {
        return dao.getAllOneCallAndCurrent()
    }
//    fun getAllLocalWeatherData(): Flow<List<WeatherData>> {
//        return dao.getWeatherDataByCityName()
//    }

    fun databaseIsEmpty(): Int = dao.databaseIsEmpty()

    fun getAllForecastData(): Flow<List<WeatherData>> =
        dao.getAllOneCallWithCurrentAndDailyAndHourly().map { weatherList ->
            weatherList.map { weather ->
                WeatherData(
                    weather.oneCall.asDomainModel(),
                    weather.current.current.asDomainModel(
                        weather.current.weather.map { it ->
                            it.asDomainModel()
                        }),
                    weather.daily.map { it.asDomainModel() }
                )
            }
        }

    fun getLocalWeatherDataByCityName(cityName: String): Flow<WeatherData> = combine(
        dao.getOneCallAndCurrentByCityName(cityName),
        dao.getCurrentWithWeatherByCityName(cityName),
        dao.getDailyByCityName(cityName)
    ) { oneCallAndCurrent, currentWeather, daily ->
        val oneCall = oneCallAndCurrent.oneCall.asDomainModel()
        val weather = currentWeather.weather.map {
            it.asDomainModel()
        }
        val current = oneCallAndCurrent.current.asDomainModel(weather)
        WeatherData(
            coordinates = oneCall,
            current = current,
            daily = daily.map { it.asDomainModel() },
//            hourly = emptyList()
        )
    }

    suspend fun insertLocalData(
        oneCall: OneCallEntity,
        current: CurrentEntity,
        currentWeather: List<CurrentWeatherEntity>,
        daily: List<DailyEntity>,
    ) {
        withContext(Dispatchers.IO) {
            dao.insertData(
                oneCall = oneCall,
                oneCallCurrent = current,
                currentWeatherList = currentWeather,
                daily = daily
            )
        }
    }
}