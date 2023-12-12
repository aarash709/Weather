package com.weather.feature.search

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

const val searchRoute = "search"

fun NavController.toSearchScreen(navOptions: NavOptions? = null) {
    navigate(searchRoute, navOptions)
}

@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@FlowPreview
fun NavGraphBuilder.searchScreen(onSearchItemSelected: () -> Unit) {
    composable(
        route = searchRoute,
        enterTransition = {
            fadeIn(tween(400))
        },
        exitTransition = {
            if (targetState.destination.route == null)
                fadeOut()
            else
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400),
                )
        }
    ) {
        SearchScreen(onSelectSearchItem = onSearchItemSelected)
    }
}