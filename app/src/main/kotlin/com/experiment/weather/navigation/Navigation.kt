package com.experiment.weather.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.experiment.weather.WeatherAppState
import com.weather.feature.managelocations.LOCATIONS_ROUTE
import com.weather.feature.managelocations.manageLocationsScreen
import com.weather.feature.managelocations.toManageLocations
import com.weather.feature.search.SEARCH_ROUTE
import com.weather.feature.search.searchScreen
import com.weather.feature.search.toSearchScreen
import com.weather.feature.settings.settingsScreen
import com.weather.feature.settings.toSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun WeatherNavHost(
    modifier: Modifier = Modifier,
    appState: WeatherAppState,
    isDatabaseEmpty: Boolean,
) {
    val startDestination = if (isDatabaseEmpty) SEARCH_ROUTE else FORECAST_ROUTE
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        forecastRoute(
            navigateToManageLocations = {
                navController.toManageLocations(navOptions {
                })
            },
            navigateToSettings = {
                navController.toSettings()
            }
        )
        manageLocationsScreen(
            onBackPressed = {
                navController.popBackStack()
            },
            onItemSelected = { _ ->
                navController.navigate(
                    route = FORECAST_ROUTE,
                    navOptions = navOptions {
                        launchSingleTop = true
                        popUpTo(
                            route = LOCATIONS_ROUTE,
                            popUpToBuilder = {
                                inclusive = true
                            }
                        )
                    })
            },
            onNavigateToSearch = { navController.toSearchScreen() }
        )
        searchScreen(onSearchItemSelected = {
            navController.toManageLocations(navOptions = navOptions {
                popUpTo(LOCATIONS_ROUTE) {
                    inclusive = true
                }
            })
        })
        settingsScreen(onBackPress = {
            navController.popBackStack()
        })

    }
}
