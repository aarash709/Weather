package com.experiment.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.experiment.weather.navigation.WeatherNavHost
import com.weather.core.design.theme.WeatherTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalAnimationApi::class,
    FlowPreview::class
)
@Composable
fun WeatherApp(
//    hasInternet: Boolean,
    isDatabaseEmpty: Boolean,
    appState: WeatherAppState = rememberWeatherAppSate(),
) {

    WeatherTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val currentDestinationRoute = appState.currentTopLevelDestination
        Scaffold(
            modifier = Modifier,
            topBar = { },
            bottomBar = { },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0),
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    WeatherNavHost(
                        modifier = Modifier,
                        appState = appState,
                        isDatabaseEmpty = isDatabaseEmpty
                    )
                }
            })
    }
}

@Composable
fun WeatherBackground(
    modifier: Modifier = Modifier,
    background: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        content()
        background()
    }
}