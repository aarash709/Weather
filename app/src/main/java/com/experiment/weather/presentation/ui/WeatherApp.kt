package com.experiment.weather.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.experiment.weather.presentation.ui.navigation.WeatherNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.weather.core.design.theme.WeatherTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@FlowPreview
@ExperimentalAnimationApi
@Composable
fun WeatherApp() {
    WeatherTheme {
        val navController = rememberAnimatedNavController()
        WeatherNavHost(navController)
    }
}