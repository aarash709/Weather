package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.weather.core.design.theme.WeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastTopBar(
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {},
        navigationIcon = {},
        actions = {
            IconButton(onClick = { onNavigateToManageLocations() }) {
                Icon(
                    imageVector = Icons.Sharp.Add,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
            }
            IconButton(onClick = { onNavigateToSettings() }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Location Pick Icon",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults
            .centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            )
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun Topbar() {
    WeatherTheme {
        ForecastTopBar(
            onNavigateToManageLocations = { },
            onNavigateToSettings = { }
        )
    }
}