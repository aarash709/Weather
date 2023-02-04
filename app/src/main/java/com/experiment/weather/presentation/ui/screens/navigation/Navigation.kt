package com.experiment.weather.presentation.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun WeatherNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Graph.Home,
        route = Graph.Root
    ) {
        homeNavGraph(navController)
        searchNavGraph(navController)
        getStartedNavGraph(navController)

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


object Graph {
    const val Home = "homeGraph?cityName={cityName}"
    const val Search = "searchGraph"
    const val GetStarted = "getStartedGraph" // this is show when user has no data yet
    const val Root = "rootGraph"// this is show when user has no data yet
    fun passHomeArgument(cityName: String):String{
        return Home.replace("{cityName}",cityName)
    }
}

//sealed class Graph(val route: String){
//    object Home : Graph("appDetails")
//    object Search :Graph("search")
//    object GetStarted :Graph("getStarted") // this is show when user has no data yet
//    object Root :Graph("root") // this is show when user has no data yet
//}
