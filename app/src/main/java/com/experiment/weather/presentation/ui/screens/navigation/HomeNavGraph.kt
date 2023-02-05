package com.experiment.weather.presentation.ui.screens.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.weather.feature.forecast.WeatherForecastScreen
import timber.log.Timber

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation(
        startDestination = Screen.MainForecast.route,
        route = Graph.Home,
    ) {
        composable(
            route = Screen.MainForecast.route,
            arguments = listOf(navArgument(name = "cityName") { nullable = true })
        ) {
            LaunchedEffect(key1 = Unit) {
                Timber.e(it.arguments?.getString("cityName"))
            }
            WeatherForecastScreen(
                navigateToSearch = {
                    navController.navigate(Graph.Search)
                },
                navigateToGetStarted = {
                    navController.navigate(Graph.GetStarted) })
        }
    }
}