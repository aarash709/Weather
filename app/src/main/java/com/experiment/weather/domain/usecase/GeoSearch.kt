package com.experiment.weather.domain.usecase

import com.experiment.weather.repository.WeatherRepository

class GeoSearch(
    private val repository: WeatherRepository
) {
//    suspend operator fun invoke(location: String): Flow<Resource<List<GeoSearchItem>>> {
//        return repository.searchLocation(location)
//    }
}