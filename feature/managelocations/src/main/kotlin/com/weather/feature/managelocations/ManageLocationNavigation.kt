package com.weather.feature.managelocations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.weather.core.design.LocationsRoute
import com.weather.core.design.SearchRoute
import kotlinx.coroutines.FlowPreview


fun NavController.toManageLocations(navOptions: NavOptions? = null) {
    navigate(LocationsRoute, navOptions)
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@FlowPreview
fun NavGraphBuilder.manageLocationsScreen(
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    composable<LocationsRoute>(
        enterTransition = {
            when {
                initialState.destination.hasRoute(SearchRoute::class) -> fadeIn(tween(350))
                else -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(350)
                )
            }
        },
        exitTransition = {
            when  {
                targetState.destination.hasRoute(SearchRoute::class) -> fadeOut(tween(350))
                else -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(350)
                )
            }
        }
    ) {
        ManageLocationsRoute(
            onBackPressed = { onBackPressed() },
            onItemSelected = { onItemSelected(it) },
            onNavigateToSearch = { onNavigateToSearch() }
        )
    }
}