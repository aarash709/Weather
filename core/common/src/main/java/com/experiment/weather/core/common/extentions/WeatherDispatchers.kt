package com.experiment.weather.core.common.extentions

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispachers(val dispatcher : WeatherDidpatchers)

enum class WeatherDidpatchers {
    IO,
}