package com.experiment.weather.presentation.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.experiment.weather.presentation.manageLocationsRoute
import com.experiment.weather.presentation.manageLocationsScreen
import com.experiment.weather.presentation.toManageLocations
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.weather.feature.search.searchScreen
import com.weather.feature.search.toSearchScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalAnimationApi
@Composable
fun WeatherNavHost(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Graph.Forecast.graph,
    ) {
        homeNavGraph(navController,
            navigateToManageLocations = {
//                navController.toManageLocations()
                navController.navigate(manageLocationsRoute)
            },
            navigateToOnboard = {
                navController.toSearchScreen()
            })
        onboardScreen(navigateToSearch = {
            navController.toSearchScreen()
        })
        manageLocationsScreen(
            onNavigateToSearch = {
                navController.toSearchScreen(navOptions = navOptions {

                })
            },
            onBackPressed = {
                navController.navigate(Graph.Forecast.graph, navOptions = navOptions {
                    popUpTo(Graph.Forecast.graph) { inclusive = true }
                })
            },
            onItemSelected = { cityName ->
                navController.navigate(
                    Graph.Forecast.passForecastArgument(cityName),
                    navOptions = navOptions {
                        popUpTo(Graph.Forecast.graph) { inclusive = true }
                    })
            }
        )
        searchScreen (onSearchItemSelected = {
            navController.toManageLocations(navOptions = navOptions {
                launchSingleTop = true
                popUpTo(manageLocationsRoute)
            })
        })
//        searchNavGraph(navController)

    }
}

sealed class Screen(val route: String) {
    object MainForecast : Screen(route = "mainForecast?cityName={cityName}") {
        fun passString(cityName: String): String {
            return this.route.replace(
                "{cityName}", cityName
            )
        }
    }

    object Search : Screen(route = "search")
    object ManageLocation : Screen(route = "manageLocation")
    object Welcome : Screen(route = "welcome")
}


//object Graph {
//    const val Home = "homeGraph?cityName={cityName}"
//    const val Search = "searchGraph"
//    const val GetStarted = "getStartedGraph" // this is show when user has no data yet
//    const val Root = "rootGraph"// this is show when user has no data yet
//    fun passHomeArgument(cityName: String):String{
//        return Home.replace("{cityName}",cityName)
//    }
//}

sealed class Graph(val graph: String) {
    object Forecast : Graph("forecast") {
        val ForecastScreen = "forecast?cityName={cityName}"
        fun passForecastArgument(cityName: String): String {
            return ForecastScreen.replace("{cityName}", cityName)
        }
    }

    object Search : Graph("search") {
        val ManageLocationScreen = "${graph}manageLocation"
        val SearchScreen = "${graph}search"
    }

    object GetStarted : Graph("getStarted") {
        val WelcomeScreen = "${graph}welcome"
        val ManageLocationScreen = "${graph}manageLocation"
        val SearchScreen = "${graph}search"
    }

    object Root : Graph("root") // this is show when user has no data yet
}
