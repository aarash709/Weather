package com.experiment.weather.presentation.ui.screens.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.experiment.weather.presentation.ui.screens.ManageLocations
import com.weather.feature.search.SearchScreen

fun NavGraphBuilder.searchNavGraph(navController: NavController) {
    navigation(
        startDestination = Screen.ManageLocation.route,
        route = Graph.Search
    ) {
        composable(route = Screen.ManageLocation.route) {
            ManageLocations(
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route){
                        popUpTo(Screen.MainForecast.route)
                        launchSingleTop = true
                    }
                },
                onBackPressed = {
                    navController.navigate(Screen.MainForecast.route)
                },
                onItemSelected = { cityName ->
                    navController.navigate(Screen.MainForecast.passString(cityName = cityName))
                })
        }
        composable(
            route = Screen.Search.route
        ) {
            SearchScreen(
                onSelectSearchItem = {
                    navController.navigate(Screen.ManageLocation.route)
                }
            )
        }
    }
}