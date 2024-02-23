package com.experiment.weather.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.weather.feature.managelocations.manageLocationsRoute
import com.weather.feature.managelocations.manageLocationsScreen
import com.weather.feature.managelocations.toManageLocations
import com.weather.feature.search.searchRoute
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
    navController: NavHostController,
    isDatabaseEmpty: Boolean,
) {
    val startDestination = if (isDatabaseEmpty) searchRoute else forecastRoute
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeNavGraph(
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
                    route = forecastRoute,
                    navOptions = navOptions {
                        launchSingleTop = true
                        popUpTo(
                            route = manageLocationsRoute,
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
                popUpTo(manageLocationsRoute) {
                    inclusive = true
                }
            })
        })
        settingsScreen(onBackPress = {
            navController.popBackStack()
        })

    }
}
