package com.weather.feature.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable

const val SETTINGS_ROUTE = "settings"
fun NavController.toSettings(navOptions: NavOptions? = null) {
    navigate(SETTINGS_ROUTE, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsScreen(onBackPress: () -> Unit) {
    composable(
        SETTINGS_ROUTE,
        enterTransition = {
            slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        }
    ) {
        Settings {
            onBackPress()
        }
    }
}