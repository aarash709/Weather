package com.experiment.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun WeatherApp(hasInternet: Boolean) {
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
                    AnimatedVisibility(visible = hasInternet.not()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = Color.Yellow.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Text(text = "No Internet")
                        }
                    }
                    WeatherNavHost(
                        modifier = Modifier,
                        navController = navController
                    )
                }
            })
    }
}
