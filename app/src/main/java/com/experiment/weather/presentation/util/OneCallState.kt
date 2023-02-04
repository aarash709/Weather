package com.experiment.weather.presentation.util

import com.experiment.weather.data.remote.dto.oncecall.OneCallDto

data class OneCallState(
    val oneCallData: OneCallDto? = null,
    val loading: Boolean = false,
)
