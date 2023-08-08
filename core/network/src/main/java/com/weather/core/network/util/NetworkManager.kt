package com.weather.core.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    val hasInternet = flow {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        emit(false)
    }
}