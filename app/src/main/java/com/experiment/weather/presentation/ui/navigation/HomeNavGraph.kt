package com.experiment.weather.presentation.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navArgument
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.weather.feature.forecast.WeatherForecastScreen
import timber.log.Timber

@ExperimentalAnimationApi
fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation(
        startDestination = Screen.MainForecast.route,
        route = Graph.Home,
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, tween(500))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, tween(500))
        }
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
                    navController.navigate(Graph.Search){
                        popUpTo(Screen.MainForecast.route){
                        }
                    }
                },
                navigateToGetStarted = {
                    navController.navigate(Graph.GetStarted) })
        }
    }
}