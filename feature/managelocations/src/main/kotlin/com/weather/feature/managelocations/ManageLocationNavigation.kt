package com.weather.feature.managelocations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.weather.feature.search.SEARCH_ROUTE
import kotlinx.coroutines.FlowPreview

const val LOCATIONS_ROUTE = "MANAGE_LOCATIONS_ROUTE"

fun NavController.toManageLocations(navOptions: NavOptions? = null) {
    navigate(LOCATIONS_ROUTE, navOptions)
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@FlowPreview
fun NavGraphBuilder.manageLocationsScreen(
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    composable(
        route = LOCATIONS_ROUTE,
        enterTransition = {
            when (initialState.destination.route) {
                SEARCH_ROUTE -> fadeIn(tween(350))
                else -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(350)
                )
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                SEARCH_ROUTE -> fadeOut(tween(350))
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