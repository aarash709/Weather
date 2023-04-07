package com.experiment.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.experiment.weather.navigation.WeatherNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
        val systemUIColors = rememberSystemUiController()
        val systemBarColor = MaterialTheme.colors.background
        val darkIcons = !isSystemInDarkTheme()
        LaunchedEffect(key1 = systemUIColors) {
            systemUIColors.apply {
                setStatusBarColor(
                    color = systemBarColor,
                    darkIcons = darkIcons
                )
                setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = darkIcons
                )

            }
        }
        WeatherNavHost(navController)
    }
}