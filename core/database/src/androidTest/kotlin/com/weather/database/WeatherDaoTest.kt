package com.weather.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.weather.core.database.WeatherDao
import com.weather.core.database.WeatherDatabase
import com.weather.core.testing.data.current
import com.weather.core.testing.data.daily
import com.weather.core.testing.data.hourly
import com.weather.core.testing.data.weatherLocationInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            WeatherDatabase::class.java
        ).build()
        weatherDao = database.dao
    }

    @After
    fun destroy() {
        database.close()
    }

    @Test
    fun weatherDao_database_is_empty_with_no_data_insert_test() {
        val isEmpty = weatherDao.countColumns() == 0
        assert(isEmpty)
    }

    @Test
    fun weatherDao_database_is_not_empty_test() = runTest {
        weatherDao.insertWeatherLocation(weatherLocation = weatherLocationInfo)
        val isEmpty = weatherDao.countColumns() == 1
        assert(isEmpty)
    }

    @Test
    fun weatherDao_weatherData_is_upsert_atomically_test() = runTest {
        weatherDao.insertData(
            weatherLocationInfo,
            current,
            daily,
            hourly
        )
        val weatherData = weatherDao
            .getAllWeatherData()
            .first()
        assertEquals(
            listOf(
                weatherData.first().weatherLocation.cityName,
                weatherData.first().current.cityName,
                weatherData.first().daily.first().cityName,
                weatherData.first().hourly.first().cityName,
            ),
            listOf(
                weatherLocationInfo.cityName,
                current.cityName,
                daily.first().cityName,
                hourly.first().cityName,

                )
        )
    }

    @Test
    fun weatherDao_upsert_then_delete_then_check_database_is_empty_test() = runTest {
        weatherDao.insertData(
            weatherLocationInfo,
            current,
            daily,
            hourly
        )
        val cityNames = listOf(weatherLocationInfo.cityName)
        weatherDao.deleteMultipleWeather(
            cityNames
        )
        val isEmpty = weatherDao.countColumns() == 0
        assert(
            isEmpty,
        )
    }
}