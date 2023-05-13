package com.weather.core.datastore

import android.app.Instrumentation
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDataStoreTest {

    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()


    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var dataStore: DataStore<Preferences>
    private val userFavoriteCity = "cityName"

    //test subject
    private lateinit var userPreferences: LocalUserPreferences

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory
            .create(
                scope = testScope,
                produceFile = {
                    tempFolder.newFile("test_user_preferences")
                })
        userPreferences = LocalUserPreferences(dataStore)
    }


    @Test
    fun `set favorite city`() = runTest {
        userPreferences.setFavoriteCity(userFavoriteCity)
//        val getCity = userPreferences.getFavoriteCity().first()
        assertEquals(userFavoriteCity, userFavoriteCity)
    }


}