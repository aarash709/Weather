package com.weather.feature.forecast.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.material.placeholder

@Composable
fun ForecastTopBar(
    cityName: String,
    showPlaceholder: Boolean,
    onNavigateToManageLocations: ()->Unit,
    onNavigateToSettings: ()->Unit,
) {
    TopAppBar(
        modifier = Modifier,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            CustomForecastTopBarRow(
                cityName = cityName,
                showPlaceholder = showPlaceholder,
                onNavigateToManageLocations = { onNavigateToManageLocations() },
                onNavigateToSettings = { onNavigateToSettings() }
            )
        }
    }
}

@Composable
private fun CustomForecastTopBarRow(
    cityName: String,
    showPlaceholder: Boolean,
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    // TODO:  //handled in the gps handler later on
    val locationBased by remember {
        mutableStateOf<Boolean>(value = false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavigateToManageLocations() }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        }
        Row(
            modifier = Modifier.padding(8.dp).placeholder(showPlaceholder),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (locationBased) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Location Icon"
                )
            }
            Text(
                text = cityName,
                modifier = Modifier,
                fontSize = 20.sp
            )
//            Icon(
//                imageVector = Icons.Default.ArrowDropDown,
//                contentDescription = "Location Picker Icon"
//            )
        }
        IconButton(
            onClick = { onNavigateToSettings() },
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Location Pick Icon"
            )
        }
    }
}