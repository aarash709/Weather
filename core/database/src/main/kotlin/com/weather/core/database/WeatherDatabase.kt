package com.weather.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.core.database.entities.meteo.CurrentEntity
import com.weather.core.database.entities.meteo.DailyEntity
import com.weather.core.database.entities.meteo.HourlyEntity
import com.weather.core.database.entities.meteo.WeatherLocationEntity

@Database(
    entities = [
        WeatherLocationEntity::class,
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