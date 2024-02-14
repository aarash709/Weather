package com.weather.feature.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SETTINGS_ROUTE = "settings"
fun NavController.toSettings(navOptions: NavOptions? = null) {
    navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsScreen(onBackPress: () -> Unit) {
    composable(
        SETTINGS_ROUTE,
        enterTransition = {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        }
    ) {
        Settings {
            onBackPress()
        }
    }
}