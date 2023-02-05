package com.experiment.weather.presentation.util

import com.weather.core.network.model.weather.NetworkOneCall


data class OneCallState(
    val oneCallData: NetworkOneCall? = null,
    val loading: Boolean = false,
)
