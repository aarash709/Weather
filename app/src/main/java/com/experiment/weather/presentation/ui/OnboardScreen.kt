package com.experiment.weather.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun OnboardScreen(navigateToSearch: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Welcome")
            Button(
                onClick = {
                    navigateToSearch()
//                        navController.navigate(Graph.GetStarted.SearchScreen) {
////                            launchSingleTop = true
//                        }
                }) {
                Text(text = "Let`s Go")
            }
        }
    }
}