package com.weather.feature.managelocations

import androidx.compose.animation.AnimatedContentTransitionScope
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
import com.weather.feature.search.searchRoute
import kotlinx.coroutines.FlowPreview

const val manageLocationsRoute = "manageLocations"

fun NavController.toManageLocations(navOptions: NavOptions? = null) {
    navigate(manageLocationsRoute, navOptions)
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@FlowPreview
fun NavGraphBuilder.manageLocationsScreen(
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
) {
    composable(
        route = manageLocationsRoute,
        enterTransition = {
            when (initialState.destination.route) {
                searchRoute -> fadeIn(tween(400))
                else -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(400)
                )
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                searchRoute -> fadeOut(tween(400))
                else -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(400)
                )
            }
        }
    ) {
        ManageLocations(
            onBackPressed = { onBackPressed() },
            onItemSelected = { onItemSelected(it) }
        )
    }
}