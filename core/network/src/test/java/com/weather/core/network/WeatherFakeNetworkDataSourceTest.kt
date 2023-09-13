package com.weather.core.network

import com.weather.core.network.model.weather.NetworkCurrent
import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.test.StandardTestDispatcher
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
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `network serialization test`() = runTest(testDispatcher) {
        val networkOnceCallSample = networkOneCallTestData
        assertEquals(networkOnceCallSample.timezone, subject.getRemoteData().data?.timezone)
    }

    @Test
    fun `network direct geoCode test`() = runTest(testDispatcher) {
        val geoCodeSample = networkDirectGeoCodeSampleData
        assertEquals(
            networkDirectGeoCodeSampleData.first().name,
            subject.directGeocode(
                geoCodeSample.first().name!!
            ).first().name
        )
    }
}

val networkOneCallTestData = NetworkOneCall(
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
val networkDirectGeoCodeSampleData = listOf(
    GeoSearchItem(
        country = "iran",
        lat = 123.0,
        local_names = null,
        lon = 123.123,
        name = "tehran",
        state = "tehranprov"
    ),
    GeoSearchItem(
        country = "USA",
        lat = 124.0,
        local_names = null,
        lon = 1234.1234,
        name = "NewYork",
        state = "NewYorkState"
    )
)