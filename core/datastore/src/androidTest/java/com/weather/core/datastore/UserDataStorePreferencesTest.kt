package com.weather.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserDataStorePreferencesTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testScope = TestScope(StandardTestDispatcher())
    private lateinit var dataStore : DataStore<Preferences>

    //test subject
    private lateinit var localPreferences : LocalUserPreferences

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = {
                testContext.preferencesDataStoreFile("preferences_test")
            }
        )
        localPreferences = LocalUserPreferences(dataStore)
    }

    @Test
    fun get_favorite_city() = testScope.runTest {
        val favCity = "tehran"
        localPreferences.setFavoriteCity(favCity)
        val savedFavCityCoordinate = localPreferences.getFavoriteCity().first()
        assertEquals(favCity, savedFavCityCoordinate)
    }

    @Test
    fun get_temperature_setting() = testScope.runTest {
        val userSetTemperature = "C"
        localPreferences.setTemperatureUnit(userSetTemperature)
        val savedTemperature = localPreferences.getTemperatureUnitSetting().first()
        assertEquals(userSetTemperature, savedTemperature)
    }

    @Test
    fun get_windSpeed_setting() = testScope.runTest {
        val userSetWindSpeed = "km"
        localPreferences.setWindSpeedUnit(userSetWindSpeed)
        val savedWindSpeed = localPreferences.getWindSpeedUnitSetting().first()
        assertEquals(userSetWindSpeed, savedWindSpeed)
    }

}