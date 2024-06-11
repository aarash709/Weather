package com.weather.feature.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

const val SEARCH_ROUTE = "SEARCH_ROUTE"

fun NavController.toSearchScreen(navOptions: NavOptions? = null) {
    navigate(SEARCH_ROUTE, navOptions)
}

@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@FlowPreview
fun NavGraphBuilder.searchScreen(onSearchItemSelected: () -> Unit) {
    composable(
        route = SEARCH_ROUTE,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        SearchRoute(onSelectSearchItem = onSearchItemSelected)
    }
}