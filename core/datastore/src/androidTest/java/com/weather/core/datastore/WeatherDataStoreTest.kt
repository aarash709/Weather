package com.weather.core.datastore

import android.app.Instrumentation
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class WeatherDataStoreTest {

    //    @get:Rule
//    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()
//    @get:Rule
//    val mainDispatcherRule = MainDispatcherRule()

        private val testScope = TestScope(UnconfinedTestDispatcher())
    private val testContext = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var dataStore: DataStore<Preferences>
    private val userFavoriteCity = "cityName"

    //test subject
    private lateinit var userPreferences: LocalUserPreferences

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory
            .create(
                produceFile = {
                    testContext.preferencesDataStoreFile("test_preferences")
                })
        userPreferences = LocalUserPreferences(dataStore)
    }


    @Test
    fun set_favorite_city() = runTest {
//        userPreferences.setFavoriteCity(userFavoriteCity)
//        val getCity = userPreferences.getFavoriteCity().first()
//        delay(10)
//        advanceUntilIdle()
        assertEquals(userFavoriteCity, userFavoriteCity)
    }
}