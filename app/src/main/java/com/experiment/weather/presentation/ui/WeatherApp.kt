package com.experiment.weather.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.experiment.weather.presentation.ui.navigation.WeatherNavigation

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    WeatherNavigation(navController)
}