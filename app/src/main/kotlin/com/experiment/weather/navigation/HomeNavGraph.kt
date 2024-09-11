package com.experiment.weather.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.weather.core.design.ForecastRoute
import com.weather.core.design.LocationsRoute
import com.weather.core.design.SettingsRoute
import com.weather.core.design.theme.ForecastTheme
import com.weather.feature.forecast.WeatherForecastRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun NavGraphBuilder.forecastRoute(
    navigateToManageLocations: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    composable<ForecastRoute>(
        enterTransition = {
            when {
                initialState.destination.hierarchy.any {
                    it.hasRoute<LocationsRoute>() || it.hasRoute<SettingsRoute>()
                } -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(350),
                        initialOffset = { it / 3 }
                    )
                }

                else -> {
                    fadeIn()
                }
            }
        },
        exitTransition = {
            when {
                targetState.destination.hierarchy.any {
                    it.hasRoute<LocationsRoute>() || it.hasRoute<SettingsRoute>()
                } -> slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350), targetOffset = { it / 3 })

                else -> {
                    fadeOut()
                }
            }
        }
    ) {
        ForecastTheme {
            WeatherForecastRoute(
                onNavigateToManageLocations = {
                    navigateToManageLocations()
                },
                onNavigateToSettings = {
                    navigateToSettings()
                })
        }
    }
}