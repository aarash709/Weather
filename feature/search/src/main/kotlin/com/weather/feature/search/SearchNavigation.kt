package com.weather.feature.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.weather.core.design.SearchRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


fun NavController.toSearchScreen(navOptions: NavOptions? = null) {
    navigate(SearchRoute, navOptions)
}

@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@FlowPreview
fun NavGraphBuilder.searchScreen(onSearchItemSelected: () -> Unit) {
    composable<SearchRoute>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        SearchRoute(onSelectSearchItem = onSearchItemSelected)
    }
}