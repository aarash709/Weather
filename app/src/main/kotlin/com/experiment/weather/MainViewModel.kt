package com.experiment.weather

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.network.util.NetworkManager
import com.weather.core.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    networkManager: NetworkManager,
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    val hasInternet: StateFlow<Boolean> = networkManager.hasInternet.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1_000),
        initialValue = false
    )

    private val _dataBaseIsEmpty = MutableStateFlow(false)
    val dataBaseIsEmpty = _dataBaseIsEmpty.asStateFlow()

    init {
        checkDatabaseIsEmpty()
    }

    private fun checkDatabaseIsEmpty() {
        viewModelScope.launch {
            val isEmpty = weatherRepository.isDatabaseEmpty() == 0
            _dataBaseIsEmpty.value = isEmpty
        }
    }

//    private fun hasNetworkConnection(context: Context): Boolean {
//        val connectivityManager = context.applicationContext
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val activeNetwork = connectivityManager.activeNetwork ?: return false
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//            return when {
//                capabilities.hasTransport(TRANSPORT_WIFI) ||
//                        capabilities.hasTransport(TRANSPORT_WIFI_AWARE) ||
//                        capabilities.hasTransport(TRANSPORT_CELLULAR) ||
//                        capabilities.hasTransport(TRANSPORT_ETHERNET) ||
//                        capabilities.hasTransport(TRANSPORT_VPN) -> true
//
//                else -> false
//            }
//        } else {
//            connectivityManager.activeNetworkInfo?.run {
//                return when (type) {
//                    TYPE_WIFI,
//                    TYPE_MOBILE,
//                    TYPE_ETHERNET,
//                    TYPE_VPN,
//                    -> true
//
//                    else -> false
//                }
//            }
//        }
//        return false
//    }
}

