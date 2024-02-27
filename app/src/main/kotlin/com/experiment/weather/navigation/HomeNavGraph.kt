package com.experiment.weather.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.weather.feature.forecast.WeatherForecastScreen
import com.weather.feature.managelocations.manageLocationsRoute
import com.weather.feature.settings.SETTINGS_ROUTE
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val FORECAST_ROUTE = "forecastRoute"

@ExperimentalCoroutinesApi
fun NavGraphBuilder.forecastRoute(
    navigateToManageLocations: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    composable(
        route = FORECAST_ROUTE,
        enterTransition = {
            when (initialState.destination.route) {
                SETTINGS_ROUTE -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400),
                        initialOffset = { it / 3 }
                    )
                }

                manageLocationsRoute -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(400),
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
                SETTINGS_ROUTE -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(400), targetOffset = { it / 3 })
                }

                manageLocationsRoute -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400), targetOffset = { it / 3 })
                }

                else -> {
                    fadeOut()
                }
            }
        },
        arguments = listOf(navArgument(name = "cityName") { nullable = true })
    ) {
        WeatherForecastScreen(
            onNavigateToManageLocations = {
                navigateToManageLocations()
            },
            onNavigateToSettings = {
                navigateToSettings()
            })
    }
}