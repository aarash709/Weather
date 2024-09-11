package com.experiment.weather.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.experiment.weather.WeatherAppState
import com.weather.core.design.ForecastRoute
import com.weather.core.design.LocationsRoute
import com.weather.core.design.SearchRoute
import com.weather.feature.forecast.forecastRoute
import com.weather.feature.managelocations.manageLocationsScreen
import com.weather.feature.managelocations.toManageLocations
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
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = if (isDatabaseEmpty) SearchRoute else ForecastRoute(),
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
                    route = ForecastRoute(),
                    navOptions = navOptions {
                        launchSingleTop = true
                        popUpTo(
                            route = LocationsRoute,
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
                popUpTo(LocationsRoute) {
                    inclusive = true
                }
            })
        })
        settingsScreen(onBackPress = {
            navController.popBackStack()
        })

    }
}
