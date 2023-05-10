package com.weather.core.network.ktor

import com.weather.core.network.BuildConfig.API_KEY
import com.weather.core.network.BuildConfig.BASE_URL
import com.weather.core.network.model.geosearch.GeoSearchItemDto
import com.weather.core.network.model.weather.NetworkOneCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
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
        exclude: String,
        appId: String,
    ): NetworkOneCall

    companion object {
//        fun create(): KtorApiService {
//            return KtorServiceImpl(
//                client = HttpClient(Android) {
//                    install(ContentNegotiation) {
//                        expectSuccess = true
//                        json(Json {
////                            ignoreUnknownKeys = true
//                            prettyPrint = true
//                            isLenient = false
////                            useAlternativeNames = true
////                            encodeDefaults = false
//                        })
//                    }
//                    install(Logging) {
//                        logger = object : Logger {
//                            override fun log(message: String) {
//                                Timber.e(message)
//                            }
//                        }
//                        level = LogLevel.ALL
//                    }
//                    HttpResponseValidator {
//                        validateResponse { response ->
//                            val error: Error = response.body()
//                            Timber.e(error.message)
//                        }
//                    }
//                }
//
//            )
//        }
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
            return client.get(BASE_URL) {
                url {
                    path("data/2.5/onecall")
                    parameters.append("lat", lat)
                    parameters.append("lon", lon)
                    parameters.append("exclude", exclude)
                    parameters.append("appid", API_KEY)
                }
            }.body()
    }

    override suspend fun getGeoSearch(
        location: String,
        limit: String,
        appId: String,
    ): List<GeoSearchItemDto> {
        try {
            val geoItems: List<GeoSearchItemDto> = client.get(BASE_URL) {
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
}