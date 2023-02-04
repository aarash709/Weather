package com.experiment.weather.domain.usecase

import com.experiment.weather.repository.WeatherRepository

class GetOneCall(
    private val repository: WeatherRepository,
) {
//    suspend operator fun invoke(
//        cityName: String,
//        lat: String,
//        lon: String,
//        appId: String,
//    ): Flow<Resource<OneCallDto>> {
//        return repository.getOneCall(
//            cityName = cityName,
//            lat = lat,
//            lon = lon,
//            appId = appId)
//    }
}