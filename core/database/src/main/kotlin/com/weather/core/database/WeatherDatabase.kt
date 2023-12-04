package com.weather.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.core.database.WeatherDao
import com.weather.core.database.entities.geoSearch.GeoSearchItemEntity
import com.weather.core.database.entities.onecall.*

@Database(
    entities = [
        GeoSearchItemEntity::class,
        OneCallMinutelyEntity::class,
        CurrentWeatherEntity::class,
        OneCallEntity::class,
        CurrentEntity::class,
        DailyEntity::class,
        OneCallHourlyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val dao: WeatherDao

     companion object {

     }
}