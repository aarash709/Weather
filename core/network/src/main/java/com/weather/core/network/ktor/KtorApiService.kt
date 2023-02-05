package com.experiment.weather.data.remote.ktor

import com.weather.core.network.model.geosearch.GeoSearchItemDto
import com.weather.core.network.model.weather.NetworkOneCall
import com.weather.core.network.retrofit.CommonValues.API_KEY
import com.weather.core.network.retrofit.CommonValues.BASE_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import timber.log.Timber

interface KtorApiService {

    suspend fun getGeoSearch(
        location: String,
        limit: String,
        appId: String,
    ): List<GeoSearchItemDto>

    suspend fun getOneCall(
        lat: String,
        lon: String,
        exclude: String = "",
        appId: String,
    ): NetworkOneCall

    companion object {
        fun create(): KtorApiService {
            return KtorServiceImpl(
                client = HttpClient(Android) {
                    install(ContentNegotiation) {
                        expectSuccess = true
                        json(Json {
//                            ignoreUnknownKeys = true
                            prettyPrint = true
                            isLenient = false
//                            useAlternativeNames = true
//                            encodeDefaults = false
                        })
                    }
                    install(Logging) {
                        logger = object : Logger {
                            override fun log(message: String) {
                                Timber.e(message)
                            }
                        }
                        level = LogLevel.ALL
                    }
                    HttpResponseValidator {
                        validateResponse { response ->
                            val error: Error = response.body()
                            Timber.e(error.message)
                        }
                    }
                }

            )
        }
    }
}

class KtorServiceImpl(
    private val client: HttpClient,
) : KtorApiService {

    override suspend fun getOneCall(
        lat: String,
        lon: String,
        exclude: String,
        appId: String,
    ): NetworkOneCall {
            return client.get("https://api.openweathermap.org/data/2.5/onecall?lat=31&lon=50&appid=$appId") {
//                Learn how to configure url builder
                    // TODO: //this does not work as intended need to learn this builder
//                url {
//                    path("data/2.5/onecall")
//                    parameters.append("lat", lat)
//                    parameters.append("lon", lon)
//                    parameters.append("exclude", exclude)
//                    parameters.append("appid", API_KEY)
//                }
            }.body()
    }

    override suspend fun getGeoSearch(
        location: String,
        limit: String,
        appId: String,
    ): List<GeoSearchItemDto> {
        try {
            val geoItems: List<GeoSearchItemDto> = client.get(BASE_URL) {
//                Learn how to configure url builder
                url {
                    path("geo/1.0/direct")
                    parameters.append("q", location)
                    parameters.append("limit", "5")
                    parameters.append("appid", API_KEY)
                }
            }.body()
            return geoItems

        } catch (e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            return emptyList()
        }
    }
    //other methods for fetching or posting data
}