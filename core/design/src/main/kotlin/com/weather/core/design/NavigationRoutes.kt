package com.weather.core.design

import kotlinx.serialization.Serializable

@Serializable
data class ForecastRoute(val cityName: String = "")

@Serializable
object SettingsRoute

@Serializable
object LocationsRoute

@Serializable
object SearchRoute
