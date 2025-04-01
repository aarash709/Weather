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
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
		val localTime = Instant.now().minus(30, ChronoUnit.MINUTES)
        val time = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm").withZone(ZoneOffset.UTC).format(localTime)
		//assert if our timestamp(database in this case) is older than our threshold minutes.
		assertTrue(forecastViewModel.isDataExpired(time, 12600, 30))
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