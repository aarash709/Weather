package com.weather.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.weather.core.database.WeatherDao
import com.weather.core.database.WeatherDatabase
import com.weather.core.database.entities.onecall.CurrentEntity
import com.weather.core.database.entities.onecall.CurrentWeatherEntity
import com.weather.core.database.entities.onecall.DailyEntity
import com.weather.core.database.entities.onecall.OneCallEntity
import com.weather.core.database.entities.onecall.OneCallHourlyEntity
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
        val isEmpty = weatherDao.databaseIsEmpty() == 0
        assert(isEmpty)
    }

    @Test
    fun weatherDao_database_is_not_empty_test() = runTest {
        weatherDao.insertOneCall(oneCall = oneCall)
        val isEmpty = weatherDao.databaseIsEmpty() == 1
        assert(isEmpty)
    }

    @Test
    fun weatherDao_weatherData_is_upsert_atomically_test() = runTest {
        weatherDao.insertData(
            oneCall,
            current,
            currentWeather,
            daily,
            hourly
        )
        val weatherData = weatherDao
            .getAllOneCallWithCurrentAndDailyAndHourly()
            .first()
        assertEquals(
            listOf(
                weatherData.first().oneCall.cityName,
                weatherData.first().current.current.cityName,
                weatherData.first().current.weather.first().cityName,
                weatherData.first().daily.first().cityName,
                weatherData.first().hourly.first().cityName,
            ),
            listOf(
                oneCall.cityName,
                current.cityName,
                currentWeather.first().cityName,
                daily.first().cityName,
                hourly.first().cityName,

                )
        )
    }

    @Test
    fun weatherDao_upsert_then_delete_then_check_database_is_empty_test() = runTest {
        weatherDao.insertData(
            oneCall,
            current,
            currentWeather,
            daily,
            hourly
        )
        weatherDao.deleteWeatherByCityName(
            oneCall.cityName,
        )
        val isEmpty = weatherDao.databaseIsEmpty() == 0
        assert(
            isEmpty,
        )
    }
}

val oneCall = OneCallEntity(
    cityName = "Sommersdale",
    lat = 4.5,
    lon = 6.7,
    timezone = "graecis",
    timezone_offset = 5109

)
val current = CurrentEntity(
    cityName = "Sommersdale",
    clouds = 8337,
    dew_point = 48.49,
    dt = 8025,
    feels_like = 50.51,
    humidity = 4375,
    pressure = 9379,
    sunrise = 3962,
    sunset = 8896,
    temp = 52.53,
    uvi = 54.55,
    visibility = 7734,
    wind_deg = 2563,
    wind_speed = 56.57

)
val currentWeather = listOf(
    CurrentWeatherEntity(
        cityName = "Sommersdale",
        description = "eius",
        icon = "docendi",
        id = 3682,
        main = "deseruisse"

    )
)
val daily = listOf(
    DailyEntity(
        cityName = "Sommersdale",
        clouds = 5294,
        dew_point = 74.75,
        dt = 8669,
        humidity = 3322,
        moon_phase = 76.77,
        moonrise = 4691,
        moonset = 6711,
        pop = 78.79,
        pressure = 4394,
        sunrise = 6929,
        sunset = 7243,
        dayTemp = 80.81,
        nightTemp = 82.83,
        id = 4976,
        main = "vivamus",
        description = "nibh",
        icon = "vestibulum",
        uvi = 84.85,
        wind_deg = 7494,
        wind_gust = 86.87,
        wind_speed = 88.89

    )
)
val hourly = listOf(
    OneCallHourlyEntity(
        cityName = "Sommersdale",
        clouds = 7050,
        dew_point = 104.105,
        dt = 9104,
        feels_like = 106.107,
        humidity = 6741,
        pop = 108.109,
        pressure = 7900,
        temp = 110.111,
        uvi = 112.113,
        visibility = 6223,
        id = 8661,
        main = "ponderum",
        description = "deterruisset",
        icon = "nostra",
        wind_deg = 1451,
        wind_gust = 114.115,
        wind_speed = 116.117
    )
)