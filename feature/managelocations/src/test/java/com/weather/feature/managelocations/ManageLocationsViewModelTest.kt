package com.weather.feature.managelocations

import com.weather.core.repository.fake.FakeUserRepository
import com.weather.core.repository.fake.FakeWeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ManageLocationsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    //Test Case
    lateinit var manageLocationsViewModel: ManageLocationsViewModel

    @Before
    fun setUp() {
        manageLocationsViewModel = ManageLocationsViewModel(
            weatherRepository = FakeWeatherRepository(),
            userRepository = FakeUserRepository()
        )
    }

    @Test
    fun `location state is initially loading`() = runTest{
        assertEquals(LocationsUIState.Loading, manageLocationsViewModel.locationsState.value)
    }

}