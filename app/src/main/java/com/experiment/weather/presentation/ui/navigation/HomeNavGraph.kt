package com.experiment.weather.presentation.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navArgument
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.weather.feature.forecast.WeatherForecastScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalAnimationApi
fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    navigateToManageLocations: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToOnboard: () -> Unit,
) {
    navigation(
        startDestination = Graph.Forecast.ForecastScreen,
        route = Graph.Forecast.graph,
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, tween(500))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, tween(500))
        }
    ) {
        composable(
            route = Graph.Forecast.ForecastScreen,
            arguments = listOf(navArgument(name = "cityName") { nullable = true })
        ) {
            LaunchedEffect(key1 = Unit) {
                Timber.e(it.arguments?.getString("cityName"))
            }
            WeatherForecastScreen(
                navigateToManageLocations = {
                    navigateToManageLocations()
//                    navController.navigate(Graph.Search.graph){
////                        popUpTo(Graph.Search.ManageLocationScreen)
//                    }
                },
                onNavigateToSettings = {
                    navigateToSettings()
                },
                navigateToOnboard = {
                    navigateToOnboard()
//                    navController.navigate(Graph.Search.SearchScreen)
                })
        }
    }
}