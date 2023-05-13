package com.weather.core.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.weather.core.datastore.LocalUserPreferences
import com.weather.model.Coordinate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder


@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    @get:Rule
    val tempFolder = TemporaryFolder.builder().build()

    val testScope = TestScope(UnconfinedTestDispatcher())

    //test subjet
    lateinit var userRepo: UserRepositoryImpl

    lateinit var datastore: DataStore<Preferences>
    lateinit var localPreferences: LocalUserPreferences

    @Before
    fun setup() {
        datastore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = {
                tempFolder.newFile("user_preferences_test.pb")
            })
        localPreferences = LocalUserPreferences(datastore = datastore)
        userRepo = UserRepositoryImpl(localPreferences)
    }

    @After
    fun clean() {
        tempFolder.delete()
    }

    @Test
    fun `get favorite user city`() = runTest {
        val coordinate = Coordinate("tehran", "0", "0")
        val favCityStringCoordinate = Json.encodeToString(coordinate)
        userRepo.setFavoriteCityCoordinate(favCityStringCoordinate)
        val savedFavCityCoordinate = userRepo.getFavoriteCityCoordinate().first()
        Assert.assertEquals(coordinate, savedFavCityCoordinate)
    }
}