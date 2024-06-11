package com.experiment.weather

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @FlowPreview
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var hasInternet by mutableStateOf(false)
        var isDatabaseEmpty by mutableStateOf(true)
        var isDataLoaded by mutableStateOf(false)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.hasInternet.collect {
                    hasInternet = it
                }

            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.isDataLoaded.collect {
                    Timber.e("dataloaded:$it")
                    isDataLoaded = it
                }

            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.dataBaseIsEmpty.onEach { isDatabaseEmpty = it }.collect {
                    Timber.e("database is empty?:$it")

                }
            }
        }

        val splashScreen = installSplashScreen()

        //keep splash screen on-screen until we have loaded our data and
        splashScreen.setKeepOnScreenCondition {
            if (!isDatabaseEmpty) !isDataLoaded else false
        }

        enableEdgeToEdge()
        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            LaunchedEffect(isDarkTheme) {
                enableEdgeToEdge(
                    navigationBarStyle = if (isDarkTheme) {
                        SystemBarStyle.dark(
                            Color.TRANSPARENT,
                        )
                    } else {
                        SystemBarStyle.light(
                            Color.TRANSPARENT,
                            Color.TRANSPARENT,
                        )
                    }
                )
            }
            WeatherApp(
                isDatabaseEmpty = isDatabaseEmpty
            )
        }
    }
}