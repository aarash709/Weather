package com.experiment.weather.presentation.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.experiment.weather.presentation.ui.ManageLocations
import com.weather.feature.search.SearchScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@FlowPreview
@ExperimentalAnimationApi
fun NavGraphBuilder.getStartedNavGraph(navController: NavController) {
    navigation(
        startDestination = Graph.GetStarted.WelcomeScreen, route = Graph.GetStarted.graph,
//        arguments = listOf(navArgument(name = "cityName") {})
    ) {
        composable(route = Graph.GetStarted.WelcomeScreen) {
            Surface {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Welcome")
                    Button(onClick = {
                        navController.navigate(Graph.GetStarted.SearchScreen) {
//                            launchSingleTop = true
                        }
                    }) {
                        Text(text = "Let`s Go")
                    }
                }
            }
        }
        composable(route = Graph.GetStarted.SearchScreen) {
            SearchScreen {
                navController.navigate(Graph.GetStarted.ManageLocationScreen) {
//                    launchSingleTop = true
                }
            }
        }
        composable(route = Graph.GetStarted.ManageLocationScreen) {
            ManageLocations(
                onNavigateToSearch = {
                    navController.navigate(Graph.GetStarted.SearchScreen) {
                        launchSingleTop = true
                    }
                },
                onBackPressed = { /*TODO*/ },
                onItemSelected = {
                    navController.navigate(Graph.Forecast.passForecastArgument(it)) {
                        popUpTo(Graph.GetStarted.ManageLocationScreen) { inclusive = true}
                    }
                }
            )
        }
    }
}