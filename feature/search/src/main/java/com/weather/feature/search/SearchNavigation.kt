package com.weather.feature.search

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.ExperimentalCoroutinesApi
//import androidx.navigation.compose.composable
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
//            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, tween(500),)
            fadeIn(tween(500))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, tween(500), )
//            fadeOut(tween(500))
        }
    ) {
        SearchScreen(onSelectSearchItem = onSearchItemSelected)
    }
}