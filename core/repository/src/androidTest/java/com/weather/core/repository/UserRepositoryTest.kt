package com.weather.core.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.core.datastore.LocalUserPreferences
import com.weather.model.Coordinate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private val dataStore = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = {
            testContext.preferencesDataStoreFile("preferences_test")
        }
    )
    private val localPreferences = LocalUserPreferences(datastore = dataStore)

    //test subject
    val userRepository = UserRepositoryImpl(userPreferences = localPreferences)

    @Before
    fun setup() {

    }

    @Test
    fun get_favorite_city() = runTest {
        val coordinate = Coordinate("tehran", "0", "0")
        val favCityStringCoordinate = Json.encodeToString(coordinate)
        userRepository.setFavoriteCityCoordinate(favCityStringCoordinate)
        val savedFavCityCoordinate = userRepository.getFavoriteCityCoordinate().first()
        advanceUntilIdle()
        assertEquals(coordinate, savedFavCityCoordinate)
    }
}