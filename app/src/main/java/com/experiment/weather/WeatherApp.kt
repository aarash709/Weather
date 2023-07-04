package com.experiment.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
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

@OptIn(ExperimentalLayoutApi::class)
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
            modifier = Modifier
                .statusBarsPadding(),
            topBar = {},
            bottomBar = {},
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0),
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    WeatherNavHost(
                        modifier = Modifier,
                        navController = navController
                    )
                }
            }
        )
    }
}
