package com.experiment.weather.data.datastore

import kotlinx.coroutines.flow.Flow

interface WeatherDataStore {

    suspend fun saveSelectCity(cityName: String)

    fun loadSelectedCity(): Flow<String>
}