package com.experiment.weather.presentation.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.experiment.weather.presentation.ui.ManageLocations
import com.weather.feature.search.SearchScreen
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalAnimationApi
fun NavGraphBuilder.searchNavGraph(navController: NavController) {
    navigation(
        startDestination = Screen.ManageLocation.route,
        route = Graph.Search
    ) {
        composable(
            route = Screen.ManageLocation.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.Search.route -> fadeIn(tween(500))
                    else -> slideIntoContainer(AnimatedContentScope.SlideDirection.Right,tween(500))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.Search.route -> fadeOut(tween(500))
                    else -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left,tween(500))
                }
            }
        ) {
            ManageLocations(
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route) {
                    }
                },
                onBackPressed = {
                    navController.navigate(Screen.MainForecast.route) {
                        popUpTo(Screen.MainForecast.route) { inclusive = true }
                    }
                },
                onItemSelected = { cityName ->
                    navController.navigate(Screen.MainForecast.passString(cityName = cityName)) {
                        popUpTo(Screen.MainForecast.route) { inclusive = true }
                    }
                })
        }
        composable(
            route = Screen.Search.route,
            enterTransition = {
                fadeIn(tween(500))
            },
            exitTransition = {
                fadeOut(tween(500))
            }
        ) {
            SearchScreen(
                onSelectSearchItem = {
                    navController.navigate(Screen.ManageLocation.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}