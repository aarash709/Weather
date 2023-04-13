package com.weather.feature.search

import com.weather.core.repository.fake.FakeUserRepository
import com.weather.core.repository.fake.FakeWeatherRepository
import com.weather.sync.work.fake.FakeSyncManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    //Test Case
lateinit var searchViewModel: SearchViewModel
    @Before
    fun setUp() {
        searchViewModel = SearchViewModel(
            syncStatus = FakeSyncManager(),
            weatherRepository = FakeWeatherRepository(),
            userRepository = FakeUserRepository()
        )
    }
    @Test
    fun `initial Search UI State Is Loading`() = runTest{
        assertEquals(SearchUIState.Loading, searchViewModel.searchUIState.value)
    }
    @Test
    fun getWeatherPreview() {
    }

    @Test
    fun getSearchUIState() {
    }

    @Test
    fun setSearchQuery() {
    }

    @Test
    fun syncWeather() {
    }
}