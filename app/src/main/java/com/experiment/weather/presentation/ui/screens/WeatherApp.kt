package com.experiment.weather.presentation.ui.screens.Components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.experiment.weather.presentation.ui.screens.navigation.WeatherNavigation
import com.experiment.weather.presentation.ui.theme.WeatherTheme

@Composable
fun WeatherApp() {
    WeatherTheme {
        val navController = rememberNavController()
        WeatherNavigation(navController)
    }
}