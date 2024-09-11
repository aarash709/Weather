package com.weather.feature.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.weather.core.design.SettingsRoute

fun NavController.toSettings(navOptions: NavOptions? = null) {
    navigate(SettingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(onBackPress: () -> Unit) {
    composable<SettingsRoute>(
        enterTransition = {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(350)
            )
        },
        exitTransition = {
            slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(350)
            )
        }
    ) {
        SettingsRoute {
            onBackPress()
        }
    }
}