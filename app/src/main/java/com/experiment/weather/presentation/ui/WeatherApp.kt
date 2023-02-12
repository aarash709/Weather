package com.experiment.weather.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.experiment.weather.presentation.ui.navigation.WeatherNavigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.weather.core.design.theme.WeatherTheme
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalAnimationApi
@Composable
fun WeatherApp() {
    WeatherTheme {
        val navController = rememberAnimatedNavController()
        WeatherNavigation(navController)
    }
}