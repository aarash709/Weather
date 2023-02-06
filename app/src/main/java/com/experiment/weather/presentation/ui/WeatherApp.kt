package com.experiment.weather.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.experiment.weather.presentation.ui.navigation.WeatherNavigation
import com.weather.core.design.theme.WeatherTheme

@Composable
fun WeatherApp() {
    WeatherTheme {
    val navController = rememberNavController()
        WeatherNavigation(navController)
    }
}