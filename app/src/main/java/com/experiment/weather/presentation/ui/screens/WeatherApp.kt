package com.experiment.weather.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.experiment.weather.presentation.ui.screens.navigation.WeatherNavigation

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    WeatherNavigation(navController)

}