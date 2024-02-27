package com.experiment.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.weather.feature.managelocations.toManageLocations
import com.weather.feature.settings.toSettings
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberWeatherAppSate(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): WeatherAppState {
    return remember(coroutineScope, navController) {
        WeatherAppState(navController = navController)
    }
}

class WeatherAppState(val navController: NavHostController) {
    val currentTopLevelDestination
        @Composable
        get() = navController.currentBackStackEntryAsState().value?.destination?.route

    fun navigateToLocations() = navController.toManageLocations()

    fun navigateToSettings() = navController.toSettings()
}