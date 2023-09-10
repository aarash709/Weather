package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.PlaceholderHighlight
import com.weather.core.design.components.shimmer
import com.weather.core.design.components.weatherPlaceholder
import com.weather.core.design.theme.WeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastTopBar(
    cityName: String,
    showPlaceholder: Boolean,
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            ForecastTitle(
                cityName = cityName,
                showPlaceholder = showPlaceholder
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable { onNavigateToManageLocations() },
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        actions = {
            Icon(
                modifier = Modifier.clickable { onNavigateToSettings() },
                imageVector = Icons.Default.Settings,
                contentDescription = "Location Pick Icon"
            )
        },
        colors = TopAppBarDefaults
            .centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
    )
}

@Composable
private fun ForecastTitle(
    cityName: String,
    showPlaceholder: Boolean,
) {
    // TODO:  //handled in the gps handler later on
    val usingLocation by remember {
        mutableStateOf(value = false)
    }
    Row(
        modifier = Modifier
            .weatherPlaceholder(
                visible = showPlaceholder,
                highlight = PlaceholderHighlight.shimmer(
                ),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = cityName,
            modifier = Modifier,
            fontSize = 20.sp
        )
        if (usingLocation) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Location Icon"
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun Topbar() {
    WeatherTheme() {
        ForecastTopBar(
            cityName = "cityName",
            showPlaceholder = true,
            onNavigateToManageLocations = { },
            onNavigateToSettings = { }
        )
    }
}