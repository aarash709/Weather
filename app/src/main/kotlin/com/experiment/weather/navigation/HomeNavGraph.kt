package com.experiment.weather.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.weather.feature.forecast.WeatherForecastScreen
import com.weather.feature.managelocations.manageLocationsRoute
import com.weather.feature.settings.SETTINGS_ROUTE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
const val forecastRoute = "forecastRoute"
@ExperimentalCoroutinesApi
fun NavGraphBuilder.homeNavGraph(
    navigateToManageLocations: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    navigation(
        startDestination = forecastRoute,
        route = forecastRoute,
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
        }
    ) {
        composable(
            route = forecastRoute,
            arguments = listOf(navArgument(name = "cityName") { nullable = true })
        ) {
            LaunchedEffect(key1 = Unit) {
                Timber.e(it.arguments?.getString("cityName"))
            }
            WeatherForecastScreen(
                navigateToManageLocations = {
                    navigateToManageLocations()
                },
                onNavigateToSettings = {
                    navigateToSettings()
                })
        }
    }
}