package com.experiment.weather.data.remote.dto.oncecall

import com.experiment.weather.data.database.entities.onecall.DailyWeatherEntity
import com.experiment.weather.data.database.entities.onecall.OneCallWeatherEntity
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) {
    fun asDatabaseModel(cityName: String): OneCallWeatherEntity {
        return OneCallWeatherEntity(
            cityName = cityName,
            description = description,
            icon = icon,
            id = id,
            main = main
        )
    }

    fun asDailyDatabaseModel(cityName: String): DailyWeatherEntity {
        return DailyWeatherEntity(
            0,
            cityName,
            description,
            icon,
            id,
            main

        )
    }
}
