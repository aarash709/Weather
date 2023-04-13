package com.weather.feature.settings

import com.weather.core.repository.fake.FakeUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    //Test Subject
    lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setUp() {
        settingsViewModel = SettingsViewModel(repository = FakeUserRepository())
    }
    @Test
    fun `initial Settings UI State Is Loading`() = runTest{
        assertEquals(SettingsUIState.Loading,settingsViewModel.settingsUIState.value)
    }
}