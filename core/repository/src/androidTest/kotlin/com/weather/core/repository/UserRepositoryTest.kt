package com.weather.core.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.core.datastore.LocalUserPreferences
import com.weather.model.Coordinate
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserRepositoryTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var dataStore : DataStore<Preferences>
    private lateinit var localPreferences : LocalUserPreferences

    //test subject
    private lateinit var userRepo : UserRepositoryImpl

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = {
                testContext.preferencesDataStoreFile("preferences_test")
            }
        )
        localPreferences = LocalUserPreferences(dataStore)
        userRepo = UserRepositoryImpl(
            userPreferences = localPreferences,
            dispatcher = testDispatcher)
    }

    @Test
    fun get_favorite_city() = testScope.runTest {
        val coordinate = Coordinate("tehran", "0", "0")
        val favCityStringCoordinate = Json.encodeToString(coordinate)
        userRepo.setFavoriteCityCoordinate(favCityStringCoordinate)
        val savedFavCityCoordinate = userRepo.getFavoriteCityCoordinate().first()
        assertEquals(coordinate, savedFavCityCoordinate)
    }

    @Test
    fun get_temperature_setting() = testScope.runTest {
        val userSetTemperatureUnit = TemperatureUnits.C
        userRepo.setTemperatureUnitSetting(userSetTemperatureUnit)
        val savedTemperatureUnit = userRepo.getTemperatureUnitSetting().first()
        assertEquals(userSetTemperatureUnit, savedTemperatureUnit)
    }

    @Test
    fun get_windSpeed_setting() = testScope.runTest {
        val userSetWindSpeedUnit = WindSpeedUnits.KM
        userRepo.setWindSpeedUnitSetting(userSetWindSpeedUnit)
        val savedWindSpeedUnit = userRepo.getWindSpeedUnitSetting().first()
        assertEquals(userSetWindSpeedUnit, savedWindSpeedUnit)
    }

}