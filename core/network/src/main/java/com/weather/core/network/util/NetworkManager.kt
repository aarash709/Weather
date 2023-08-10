package com.weather.core.network.util

import kotlinx.coroutines.flow.Flow


interface NetworkManager {

    val hasInternet: Flow<Boolean>

}