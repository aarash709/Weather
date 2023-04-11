package com.weather.feature.forecast

import androidx.lifecycle.SavedStateHandle
import com.weather.core.repository.UserRepository
import com.weather.feature.forecast.fake.FakeSyncManager
import com.weather.feature.forecast.fake.FakeUserRepository
import com.weather.feature.forecast.fake.FakeWeatherRepository
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import com.weather.sync.work.utils.SyncManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var forecastViewModel: ForecastViewModel
    private val fakeWeatherRepository = FakeWeatherRepository()
    private val fakeSyncManager = FakeSyncManager()
    private val fakeUserRepository = FakeUserRepository()

    @Before
    fun setUp() {
        forecastViewModel = ForecastViewModel(
            syncStatus = fakeSyncManager,
            weatherRepository = fakeWeatherRepository,
            userRepository = fakeUserRepository,
            savedStateHandle = SavedStateHandle().apply {
                set("cityKey", "Tehran")
            }
        )
    }

    @Test
    fun `Test Data expiration Calculation`() {
        val sampleTime = 1681033210
        val thirtyMinutesBeforeNow = Duration.ofMillis(System.currentTimeMillis()).minus(Duration.ofMinutes(30)).toSeconds().toInt()
        //assert if our timestamp(database in this case) is older than our threshold minutes.
        assertTrue(forecastViewModel.isDataExpired(thirtyMinutesBeforeNow, 20))
    }

    @Test
    fun `Test Get user settings data`() = runTest {
        val userSetting = combine(
            fakeUserRepository.getTemperatureUnitSetting(),
            fakeUserRepository.getWindSpeedUnitSetting()
        ) { temp, wind ->
            SettingsData(
                wind,
                temp
            )
        }.collect()
        val getWeatherUIState = forecastViewModel.getUserSettings().collect()
        assertEquals(
            getWeatherUIState, userSetting
        )
    }

    @After
    fun tearDown() {
    }
}