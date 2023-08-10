package com.weather.core.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.InetSocketAddress
import javax.inject.Inject

class ConnectivityMonitor @Inject constructor(
    @ApplicationContext context: Context,
) : NetworkManager {
    override val hasInternet = callbackFlow {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java)
        //prevents NPE when all network interfaces are off
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                channel.trySend(true)
                launch(Dispatchers.IO) {
                    val hasInternetAccess = hasInternetAccess(network)
                    withContext(Dispatchers.Main){
                        if (hasInternetAccess){
                            channel.send(true)
                        }else{
                            channel.send(false)
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Timber.e("on lost")
                channel.trySend(false)
            }
        }

        val request =
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
        connectivityManager.registerNetworkCallback(request, callBack)

        /**
         * immediately send the network status on app startup
         * so we don`t notify "no internet" to the user for no reason
          */
        val isConnected = connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false

        channel.trySend(isConnected)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callBack)
        }
    }

    private fun hasInternetAccess(network: Network): Boolean {
        return try {
            val socket = network.socketFactory.createSocket()
            socket.let {
                it.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                it.close()
                it.isConnected
            }
        } catch (e: Exception) {
            false
        }
    }
}