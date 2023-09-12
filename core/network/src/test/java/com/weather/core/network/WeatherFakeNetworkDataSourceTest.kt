package com.weather.core.network

import com.weather.core.network.model.weather.NetworkCurrent
import com.weather.core.network.model.weather.NetworkOneCall
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherFakeNetworkDataSourceTest {

    private lateinit var subject: FakeWeatherRemoteDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        subject = FakeWeatherRemoteDataSource(
            IODispatcher = testDispatcher
        )
    }

    @Test
    fun `network serialization test`() = runTest(testDispatcher) {
        val sampleData = NetworkOneCall(
            current = NetworkCurrent(
                clouds = 8117,
                dew_point = 14.15,
                dt = 5003,
                feels_like = 16.17,
                humidity = 9320,
                pressure = 6578,
                sunrise = 2961,
                sunset = 6806,
                temp = 18.19,
                uvi = 20.21,
                visibility = 6648,
                wind_deg = 6847,
                wind_speed = 22.23,
                weather = listOf()
            ),
            lat = 24.25,
            lon = 26.27,
            timezone = "tehran",
            timezone_offset = 3977,
            daily = listOf(),
            hourly = listOf()

        )
        assertEquals(sampleData.timezone, subject.getRemoteData().data?.timezone)
    }
}