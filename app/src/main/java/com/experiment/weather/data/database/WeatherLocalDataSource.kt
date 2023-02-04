package com.experiment.weather.data.database

import com.experiment.weather.data.database.entities.relation.OneCallAndCurrent
import com.experiment.weather.data.remote.dto.oncecall.OneCallDto
import com.experiment.weather.data.remote.model.weatherData.WeatherData
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

    fun databaseIsEmpty():Int = dao.databaseIsEmpty()

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

    suspend fun insertLocalData(cityName: String, remoteData: OneCallDto) {
//        removeExpired(cityName)
        //insert latest
        withContext(Dispatchers.IO) {
            dao.insertData(remoteData.toOneCallEntity(cityName),
                remoteData.current.asDatabaseModel(cityName), remoteData.current.weather.map {
                    it.asDatabaseModel(cityName)
                })
//            dao.insertOneCall(remoteData.toOneCallEntity(cityName))
//            dao.insertOneCallCurrent(remoteData.current.asDatabaseModel(cityName))
//            dao.insertOneCallCurrentWeather(remoteData.current.weather.map {
//                it.asDatabaseModel(cityName)
//            })
        }
    }

    private suspend fun removeExpired(cityName: String) {
        dao.deleteOneCallByCityName(cityName)
        dao.deleteOneCallCurrentByCityName(cityName)
        dao.deleteCurrentWeatherByCityName(cityName)
    }
}