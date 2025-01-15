package com.weather.feature.forecast

import com.weather.core.repository.fake.FakeUserRepository
import com.weather.core.repository.fake.FakeWeatherRepository
import com.weather.model.SettingsData
import com.weather.sync.work.fake.FakeSyncManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

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
//            savedStateHandle = SavedStateHandle().apply {
//                set("cityKey", "Tehran")
//            }
        )
    }

    @Test
    fun `Test Data expiration Calculation`() {
        val sampleTime = 1681033210
//        val thirtyMinutesBeforeNow = Duration.ofMillis(System.currentTimeMillis()).minus(Duration.ofMinutes(30)).toSeconds().toInt()
//        val instantNow = Instant.ofEpochMilli(System.currentTimeMillis())
//        val time = "2025-01-12T12:00"
//        val thirtyMinutesBeforeNow = DateTimeFormatter.ISO_DATE_TIME.
//            .format(Instant.ofEpochMilli(System.currentTimeMillis()).minusSeconds(180))
        val formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm").format(LocalDateTime.now().minusMinutes(30))
        //assert if our timestamp(database in this case) is older than our threshold minutes.
        assertTrue(forecastViewModel.isDataExpired(formatter, 20))
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
}