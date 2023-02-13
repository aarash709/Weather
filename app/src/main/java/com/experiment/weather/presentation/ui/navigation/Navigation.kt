package com.experiment.weather.presentation.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalAnimationApi
@Composable
fun WeatherNavigation(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Graph.Forecast.graph,
        route = Graph.Root.graph
    ) {
        getStartedNavGraph(navController)
        homeNavGraph(navController)
        searchNavGraph(navController)

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

sealed class Graph(val graph: String){
    object Forecast : Graph("forecast"){
        val ForecastScreen = "forecast?cityName={cityName}"
        fun passForecastArgument(cityName: String): String {
            return ForecastScreen.replace("{cityName}", cityName)
        }
    }
    object Search :Graph("search"){
        val ManageLocationScreen = "${graph}manageLocation"
        val SearchScreen = "${graph}search"
    }
    object GetStarted :Graph("getStarted") {
        val WelcomeScreen = "${graph}welcome"
        val ManageLocationScreen = "${graph}manageLocation"
        val SearchScreen = "${graph}search"
    }
    object Root :Graph("root") // this is show when user has no data yet
}
