package com.weather

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.core.database.WeatherDao
import com.weather.entities.geoSearch.GeoSearchItemEntity
import com.weather.entities.onecall.*

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

    //injected in dagger hilt
    /* companion object {
         @Volatile
         private var INSTANCE: WeatherDatabase? = null

         fun getInstance(context: Context): WeatherDatabase {
             synchronized(this) {
                 var instance = INSTANCE
                 if (instance == null) {
                     instance = Room.databaseBuilder(
                         context.applicationContext,
                         WeatherDatabase::class.java,
                         "weather_database"
                     ).fallbackToDestructiveMigration()
                         .build()
                     INSTANCE = instance
                 }
                 return instance
             }
         }
     }*/
}