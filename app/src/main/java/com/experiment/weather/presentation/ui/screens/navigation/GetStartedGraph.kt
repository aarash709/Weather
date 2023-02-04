package com.experiment.weather.presentation.ui.screens.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.experiment.weather.presentation.ui.screens.ManageLocations
import com.experiment.weather.presentation.ui.screens.SearchScreen


fun NavGraphBuilder.getStartedNavGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Welcome.route, route = Graph.GetStarted,
//        arguments = listOf(navArgument(name = "cityName") {})
    ) {
        composable(route = Screen.Welcome.route) {
            Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
                Text(text = "Welcome")
                Button(onClick = {
                    navController.navigate(Screen.Search.route) {
                        popUpTo(Graph.Home)
                        launchSingleTop = true
                    }
                }) {
                    Text(text = "Let`s Go")
                }
            }
        }
        composable(route = Screen.Search.route) {
            SearchScreen {
                navController.navigate(Screen.ManageLocation.route) {
                    launchSingleTop = true
                }
            }
        }
        composable(route = Screen.ManageLocation.route) {
            ManageLocations(
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route) {
                        launchSingleTop = true
                    }
                },
                onBackPressed = { /*TODO*/ },
                onItemSelected = {
                    navController.navigate(Graph.passHomeArgument(it)) {
                        popUpTo(Graph.GetStarted)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}