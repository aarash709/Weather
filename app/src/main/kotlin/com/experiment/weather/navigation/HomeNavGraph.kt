package com.experiment.weather.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.weather.feature.forecast.WeatherForecastRoute
import com.weather.feature.managelocations.LOCATIONS_ROUTE
import com.weather.feature.settings.SETTINGS_ROUTE
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val FORECAST_ROUTE = "FORECAST_ROUTE"

@ExperimentalCoroutinesApi
fun NavGraphBuilder.forecastRoute(
    navigateToManageLocations: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    composable(
        route = FORECAST_ROUTE,
        enterTransition = {
            when (initialState.destination.route) {
                SETTINGS_ROUTE, LOCATIONS_ROUTE -> {
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
            when (targetState.destination.route) {
                SETTINGS_ROUTE, LOCATIONS_ROUTE -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(350), targetOffset = { it / 3 })
                }

                else -> {
                    fadeOut()
                }
            }
        },
        arguments = listOf(navArgument(name = "cityName") { nullable = true })
    ) {
        WeatherForecastRoute(
            onNavigateToManageLocations = {
                navigateToManageLocations()
            },
            onNavigateToSettings = {
                navigateToSettings()
            })
    }
}