package com.weather

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideWeatherDatabase(
        @ApplicationContext context: Context,
    ): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weahter_db"
        ).allowMainThreadQueries()
            .build()
    }
    @Singleton
    @Provides
    fun provideWeatherLocalDataSource(
        db: WeatherDatabase,
    ): WeatherLocalDataSource = WeatherLocalDataSource(db.dao)

}