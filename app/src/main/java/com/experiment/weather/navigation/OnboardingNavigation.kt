package com.experiment.weather.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
//import androidx.navigation.compose.composable
import kotlinx.coroutines.launch

const val onboardNavigationRoute = "onboardRoute"
//fun NavController.toOnboard(navOptions: NavOptions? = null) {
//    navigate(route = onboardNavigationRoute, navOptions = navOptions)
//}
//
//fun NavGraphBuilder.onboardScreen(navigateToSearch: () -> Unit) {
//    composable(onboardNavigationRoute) {
//        val scope = rememberCoroutineScope()
//        LaunchedEffect(Unit){
//            launch { navigateToSearch() }
//        }
//    }
//}