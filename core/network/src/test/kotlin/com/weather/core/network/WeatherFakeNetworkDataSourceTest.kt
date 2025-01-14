package com.weather.core.network

import com.weather.core.network.model.meteoweahter.Current
import com.weather.core.network.model.meteoweahter.CurrentUnits
import com.weather.core.network.model.meteoweahter.NetworkCurrent
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
        assertEquals(networkOnceCallSample.timezone, subject.getRemoteData().getOrNull()?.timezone)
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

val networkOneCallTestData = NetworkCurrent(
    elevation = 86.87,
    generationTimeMs = 88.89,
    latitude = 90.91,
    longitude = 92.93,
    timezone = "sagittis",
    timezoneAbbreviation = "ne",
    utcOffsetSeconds = 2011,
    currentUnits = CurrentUnits(
        apparentTemperature = "tempor",
        interval = "vim",
        isDay = "vituperata",
        precipitation = "varius",
        pressureMsl = "tale",
        relativeHumidity2m = "tractatos",
        surfacePressure = "phasellus",
        temperature2m = "doctus",
        time = "melius",
        weatherCode = "hac",
        windDirection10m = "dico",
        windSpeed10m = "no"
    ),
    current = Current(
        apparentTemperature = 94.95,
        interval = 8725,
        isDay = 8925,
        precipitation = 96.97,
        pressureMsl = 98.99,
        relativeHumidity2m = 5104,
        surfacePressure = 100.101,
        temperature2m = 102.103,
        time = "liber",
        weatherCode = 8556,
        windDirection10m = 2997,
        windSpeed10m = 104.105
    )


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