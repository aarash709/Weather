package com.weather.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.core.database.entities.geoSearch.GeoSearchItemEntity
import com.weather.core.database.entities.onecall.meteo.CurrentEntity
import com.weather.core.database.entities.onecall.meteo.DailyEntity
import com.weather.core.database.entities.onecall.meteo.HourlyEntity
import com.weather.core.database.entities.onecall.meteo.WeatherLocationEntity

@Database(
    entities = [
        GeoSearchItemEntity::class,
//        OneCallMinutelyEntity::class,
//        CurrentWeatherEntity::class,
        WeatherLocationEntity::class,
//        OneCallEntity::class,
        CurrentEntity::class,
        DailyEntity::class,
        HourlyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val dao: WeatherDao

}