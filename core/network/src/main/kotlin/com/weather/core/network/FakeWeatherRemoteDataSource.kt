package com.weather.core.network

import com.experiment.weather.core.common.extentions.WeatherCoroutineDispatchers
import com.experiment.weather.core.common.extentions.WeatherDispatchers
import com.weather.core.network.model.weather.NetworkCurrent
import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.model.Resource
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FakeWeatherRemoteDataSource(
    @WeatherCoroutineDispatchers(WeatherDispatchers.IO) val dispatcher : CoroutineContext
) {

    suspend fun getRemoteData(): Resource<NetworkOneCall> {
        return withContext(dispatcher) {
            Resource.Success(
                data = NetworkOneCall(
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
            )
        }
    }

    suspend fun directGeocode(cityName: String): List<GeoSearchItem> {
        return withContext(dispatcher) {
            listOf(
                GeoSearchItem(
                    country = "iran",
                    lat = 123.0,
                    local_names = null,
                    lon = 123.123,
                    name = "tehran",
                    state = "tehranprov"
                )
            )
        }
    }

}