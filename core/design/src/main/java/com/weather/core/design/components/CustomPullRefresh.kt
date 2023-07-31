package com.weather.core.design.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomIndicator(
    modifier: Modifier = Modifier,
    state: PullRefreshState,
    isRefreshing: Boolean,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Crossfade(targetState = isRefreshing, label = "Custom refresh Indicator") { refreshing ->
            if (refreshing) {
                Row(
                    modifier = modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(
                        color = LocalContentColor.current,
                    )
                }
            } else {
                Row(
                    modifier = modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    Crossfade(targetState = state.progress >= 1, label = "pull trigger") {
                        if (it) {
                            Text(text = "release to refresh")
                            Icon(
                                imageVector = Icons.Default.KeyboardDoubleArrowUp,
                                contentDescription = ""
                            )
                        } else {
                            Text(text = "pull")
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                        }
                    }

                }
            }
        }
    }
}