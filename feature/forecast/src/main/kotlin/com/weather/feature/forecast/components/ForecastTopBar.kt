package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Segment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.PlaceholderHighlight
import com.weather.core.design.components.shimmer
import com.weather.core.design.components.weatherPlaceholder
import com.weather.core.design.theme.WeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastTopBar(
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
//            ForecastTitle(
//                cityName = cityName,
//                showPlaceholder = showPlaceholder
//            )
        },
        navigationIcon = {
//
        },
        actions = {
            IconButton(onClick = { onNavigateToManageLocations() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
            }
            IconButton(onClick = { onNavigateToSettings() }) {
                Icon(
                    imageVector = Icons.Default.Segment,
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
            fontSize = 20.sp,
            color = Color.White
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
    WeatherTheme {
        ForecastTopBar(
            onNavigateToManageLocations = { },
            onNavigateToSettings = { }
        )
    }
}