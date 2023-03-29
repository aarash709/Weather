package com.experiment.weather

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    app: Application,
//    private val useCases: WeatherUseCases,
) : AndroidViewModel(app) {

//    private val _address = MutableLiveData<List<Address>?>()
//    val address: LiveData<List<Address>?>
//        get() = _address
//    private val geocoder: Geocoder by lazy {
//        Geocoder(getApplication(), Locale.getDefault())
//    }

    private fun hasNetworkConnection(): Boolean {
        val connectivityManager = getApplication<WeatherApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) ||
                        capabilities.hasTransport(TRANSPORT_WIFI_AWARE) ||
                        capabilities.hasTransport(TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(TRANSPORT_ETHERNET) ||
                        capabilities.hasTransport(TRANSPORT_VPN) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI,
                    TYPE_MOBILE,
                    TYPE_ETHERNET,
                    TYPE_VPN,
                    -> true
                    else -> false
                }
            }
        }
        return false
    }
}

