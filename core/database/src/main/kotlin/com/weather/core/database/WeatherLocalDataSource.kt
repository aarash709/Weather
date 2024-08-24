package com.weather.core.database

import com.weather.core.database.entities.onecall.CurrentEntity
import com.weather.core.database.entities.onecall.CurrentWeatherEntity
import com.weather.core.database.entities.onecall.DailyEntity
import com.weather.core.database.entities.onecall.OneCallEntity
import com.weather.core.database.entities.onecall.OneCallHourlyEntity
import com.weather.core.database.entities.relation.OneCallAndCurrent
import com.weather.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class WeatherLocalDataSource(
    private val dao: WeatherDao,
) {

    fun getAllLocalWeatherData(): Flow<List<OneCallAndCurrent>> {
        return dao.getAllOneCallAndCurrent().map { it.sortedBy { it.oneCall.orderIndex } }
    }

    suspend fun reorderData() {
        dao.reorderOneCallWeatherData()
    }

    fun deleteDaily(cityName: String, timeStamp: Long) = dao.deleteDaily(
        cityName = cityName,
        timeStamp = timeStamp
    )

    fun deleteHourly(cityName: String, timeStamp: Int) = dao.deleteHourly(
        cityName = cityName,
        timeStamp = timeStamp
    )

    fun databaseIsEmpty(): Boolean = dao.countOneCall() == 0
    suspend fun deleteWeatherByCityName(cityNames: List<String>) =
        dao.deleteWeatherByCityName(cityName = cityNames)


    fun getAllForecastData(): Flow<List<WeatherData>> =
        dao.getAllOneCallWithCurrentAndDailyAndHourly().map { weatherList ->
            weatherList.map { weather ->
                WeatherData(
                    weather.oneCall.asDomainModel(),
                    weather.current.current.asDomainModel(
                        weather.current.weather.map { it ->
                            it.asDomainModel()
                        }),
                    weather.daily.map { it.asDomainModel() },
                    weather.hourly.map { it.asDomainModel() }
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
            hourly = emptyList()
        )
    }

    suspend fun insertLocalData(
        oneCall: OneCallEntity,
        current: CurrentEntity,
        currentWeather: List<CurrentWeatherEntity>,
        daily: List<DailyEntity>,
        hourly: List<OneCallHourlyEntity>,
    ) {
        val dataBaseCount = dao.countOneCall()
        val isCityExists =
            dao.checkIfCityExists(cityName = oneCall.cityName) == 1
        val orderIndex = if (isCityExists) dao.getOneCallAndCurrentByCityName(oneCall.cityName)
            .first().oneCall.orderIndex
        else {
            dao.getAllOneCallAndCurrent()
                .first()
                .map { it.oneCall.orderIndex!! }
                .sumOf { it + 1 }
        }
        val oneCallWithOrderIndex = if (dataBaseCount == 0) {
            oneCall.copy(orderIndex = 1)
        } else {
            oneCall.copy(orderIndex = orderIndex)
        }
        dao.insertData(
            oneCall = oneCallWithOrderIndex,
            oneCallCurrent = current,
            currentWeatherList = currentWeather,
            daily = daily,
            hourly = hourly
        )
    }
}