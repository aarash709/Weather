package com.experiment.weather.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.experiment.weather.presentation.ui.ManageLocations
import com.experiment.weather.presentation.ui.navigation.Graph
import com.google.accompanist.navigation.animation.composable
import com.weather.feature.search.SearchScreen
import com.weather.feature.search.searchRoute
import kotlinx.coroutines.FlowPreview

const val manageLocationsRoute = "manageLocations"

fun NavController.toManageLocations(navOptions: NavOptions? = null) {
    navigate(manageLocationsRoute, navOptions)
}

@ExperimentalAnimationApi
@FlowPreview
fun NavGraphBuilder.manageLocationsScreen(
    onNavigateToSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
) {
    composable(
        route = manageLocationsRoute,
        enterTransition = {
            when (initialState.destination.route) {
                searchRoute -> fadeIn(tween(500))
                else -> slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    tween(500)
                )
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                searchRoute -> fadeOut(tween(500))
                else -> slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    tween(500)
                )
            }
        }
    ) {
        ManageLocations(
            onNavigateToSearch = { onNavigateToSearch() },
            onBackPressed = { onBackPressed() },
            onItemSelected = { onItemSelected(it) }
        )
    }
}