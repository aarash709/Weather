package com.experiment.weather.core.common.extentions

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WeatherCoroutineDispatchers(val dispatcher : WeatherDispatchers)

enum class WeatherDispatchers {
    IO,
}