package com.weather.feature.managelocations

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.composable
import com.weather.feature.search.SearchScreen
import com.weather.feature.search.searchRoute
import kotlinx.coroutines.FlowPreview

const val manageLocationsRoute = "manageLocations"

fun NavController.toManageLocations(navOptions: NavOptions? = null) {
    navigate(manageLocationsRoute, navOptions)
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
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
                searchRoute -> fadeIn(tween(400))
                else -> slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    tween(400)
                )
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                searchRoute -> fadeOut(tween(400))
                else -> slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    tween(400)
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