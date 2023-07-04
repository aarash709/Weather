package com.experiment.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.experiment.weather.navigation.WeatherNavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.weather.core.design.theme.WeatherTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalAnimationApi
@Composable
fun WeatherApp() {
    WeatherTheme {
        val navController = rememberNavController()
        val systemUIColors = rememberSystemUiController()
        val systemBarColor = MaterialTheme.colorScheme.background
        val darkIcons = !isSystemInDarkTheme()

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = systemUIColors) {
            systemUIColors.apply {
                setStatusBarColor(
                    color = systemBarColor,
                    darkIcons = darkIcons
                )
                setNavigationBarColor(color = Color.Transparent)
            }
        }

        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {},
            bottomBar = {},
            snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
            content = { padding ->
                Column(modifier = Modifier.padding(horizontal = 16.dp )) {
                    WeatherNavHost(
                        modifier = Modifier,
                        navController = navController
                    )
                }
            }
        )
    }
}
