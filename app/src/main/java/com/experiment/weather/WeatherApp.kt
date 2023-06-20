package com.experiment.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

        val snackbarHostState = remember { SnackbarHostState() }
        val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)

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
            modifier = Modifier,
            scaffoldState = scaffoldState,
            topBar = {},
            bottomBar = {},
            snackbarHost = { snackbarState ->
                SnackbarHost(hostState = snackbarState)
            },
            backgroundColor = MaterialTheme.colors.background,
            contentColor = Color.Transparent,
            content = { contentPadding ->
                AnimatedBackground(modifier = Modifier.padding(contentPadding),
                    content = {
                        WeatherNavHost(navController)
                    })
            }
        )
    }
}

@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(0.dp))
        content()
    }
}
