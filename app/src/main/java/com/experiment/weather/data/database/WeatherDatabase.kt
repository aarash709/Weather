package com.experiment.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.experiment.weather.data.database.entities.geoSearch.GeoSearchItemEntity
import com.experiment.weather.data.database.entities.onecall.*

@Database(
    entities = [
        GeoSearchItemEntity::class,
        OneCallMinutelyEntity::class,
        OneCallWeatherEntity::class,
        OneCallEntity::class,
        OneCallCurrentEntity::class,
        OneCallDailyEntity::class,
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