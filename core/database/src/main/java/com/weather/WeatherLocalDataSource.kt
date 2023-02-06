package com.weather

import com.weather.entities.onecall.OneCallCurrentEntity
import com.weather.entities.onecall.OneCallEntity
import com.weather.entities.onecall.OneCallWeatherEntity
import com.weather.entities.relation.OneCallAndCurrent
import com.weather.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext

class WeatherLocalDataSource(
    private val dao: WeatherDao,
) {

    fun getAllLocalWeatherData(): Flow<List<OneCallAndCurrent>> {
        return dao.getAllOneCallAndCurrent()
    }

    fun databaseIsEmpty(): Int = dao.databaseIsEmpty()

    private fun zip(cityName: String) =
        dao.getOneCallAndCurrentByCityName(cityName)
            .zip(dao.getOneCallCurrentWithWeatherByCityName(cityName)) { oneCallAndCurrent, currentWeather ->
                val oneCall = oneCallAndCurrent.oneCall.asDomainModel()
                val weather = currentWeather.weather.map {
                    it.asDomainModel()
                }
                val current = oneCallAndCurrent.current.asDomainModel(weather)
                WeatherData(
                    coordinates = oneCall,
                    current = current,
//            daily = emptyList(),
//            hourly = emptyList()
                )
            }.flowOn(Dispatchers.IO)

    fun getLocalData(cityName: String): Flow<WeatherData> = zip(cityName)

    suspend fun insertLocalData(
        oneCall: OneCallEntity,
        current: OneCallCurrentEntity,
        currentWeather: List<OneCallWeatherEntity>,
    ) {
        withContext(Dispatchers.IO) {
            dao.insertData(
                oneCall,
                current,
                currentWeather
            )
        }
    }
}