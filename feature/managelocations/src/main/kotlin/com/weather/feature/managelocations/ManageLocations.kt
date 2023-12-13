package com.weather.feature.managelocations

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weather.core.design.components.CustomTopBar
import com.weather.core.design.components.ShowLoadingText
import com.weather.core.design.modifiers.bouncyTapEffect
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData

@ExperimentalFoundationApi
@Composable
fun ManageLocations(
    viewModel: ManageLocationsViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
) {
    //stateful
    val dataState by viewModel.locationsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        ManageLocations(
            dataState = dataState,
            onNavigateToSearch = { onNavigateToSearch() },
            onBackPressed = onBackPressed,
            onItemSelected = { coordinate ->
                onItemSelected(coordinate.cityName.toString())
            },
            onDeleteItem = { cityNames ->
                viewModel.deleteWeatherByCityName(cityNames = cityNames, context = context)
            },
            onSetFavoriteItem = { locationData ->
                val coordinate = Coordinate(
                    cityName = locationData.locationName,
                    latitude = locationData.latitude,
                    longitude = locationData.longitude
                )
                viewModel.saveFavoriteCityCoordinate(coordinate = coordinate, context = context)
            }
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun ManageLocations(
    dataState: LocationsUIState,
    onNavigateToSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onItemSelected: (Coordinate) -> Unit,
    onDeleteItem: (List<String>) -> Unit,
    onSetFavoriteItem: (ManageLocationsData) -> Unit,
) {
    var selectedCities by rememberSaveable {
        mutableStateOf(emptySet<String>())
    }
    val isInEditMode by remember {
        derivedStateOf { selectedCities.isNotEmpty() }
    }
    var locationsList by remember {
        mutableStateOf(emptyList<ManageLocationsData>())
    }
    var isAllSelected by rememberSaveable {
        mutableStateOf(false)
    }
    var itemsToDelete by remember() {
        mutableStateOf(listOf<String>())
    }
    LaunchedEffect(key1 = isAllSelected) {
        if (isAllSelected) {
            selectedCities += locationsList.map { it.locationName }
        } else {
            selectedCities = emptySet()
        }
    }
    LaunchedEffect(key1 = selectedCities) {
        itemsToDelete = selectedCities.toList()
    }
    BackHandler(enabled = isInEditMode) {
        if (isInEditMode)
            selectedCities = emptySet()
    }
    Scaffold(
        modifier = Modifier,
        topBar = {
            CustomTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
                text = if (isInEditMode) {
                    "Selected Items ${selectedCities.size}"
                } else {
                    "Manage Locations"
                },
                onBackPressed = { onBackPressed() },
                navigationIcon = {
                    AnimatedContent(
                        targetState = isInEditMode,
                        label = "Top bar Icon"
                    ) { isInEditMode ->
                        if (isInEditMode) {
                            IconButton(onClick = { selectedCities = emptySet() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    contentDescription = "Clear selection Button"
                                )
                            }
                        } else {
                            IconButton(onClick = { onBackPressed() }) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    contentDescription = "back icon"
                                )
                            }
                        }
                    }
                },
                actions = {
                    if (isInEditMode) IconButton(onClick = { isAllSelected = !isAllSelected }) {
                        Icon(
                            imageVector = Icons.Default.ChecklistRtl,
                            contentDescription = "Select all button"
                        )
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isInEditMode,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column {
                            IconButton(onClick = {
                                onDeleteItem(itemsToDelete)
                                selectedCities = emptySet()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete button"
                                )

                            }
                            Text(text = "Delete")
                        }
                    }
                }
            }
        }
    ) { padding ->
        when (dataState) {
            is LocationsUIState.Loading -> ShowLoadingText()
            is LocationsUIState.Success -> {
                LaunchedEffect(key1 = dataState) {
                    locationsList = dataState.data
                }
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchBarCard(onNavigateToSearch)
                    Spacer(modifier = Modifier.height(16.dp))

                    val inSelectionMode by remember {
                        derivedStateOf { selectedCities.isNotEmpty() }
                    }
                    if (dataState.data.isEmpty()) {
                        Text(
                            text = "Search and add a location",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .75f),
                            fontSize = 12.sp
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = dataState.data,
                                key = {
                                    it.locationName
                                }) { locationData ->
                                val selected by remember {
                                    derivedStateOf { locationData.locationName in selectedCities }
                                }
                                SavedLocationItem(
                                    modifier = Modifier.bouncyTapEffect() then
                                            if (inSelectionMode) {
                                                Modifier.clickable {
                                                    if (selected)
                                                        selectedCities -= locationData.locationName
                                                    else
                                                        selectedCities += locationData.locationName
                                                }
                                            } else {
                                                Modifier.combinedClickable(
                                                    onClick = {
                                                        onItemSelected(
                                                            Coordinate(
                                                                locationData.locationName,
                                                                locationData.latitude,
                                                                locationData.longitude
                                                            )
                                                        )
                                                    },
                                                    onLongClick = {
                                                        selectedCities += locationData.locationName
                                                    }
                                                )
                                            },
                                    data = locationData,
                                    inSelectionMode = inSelectionMode,
                                    selected = selected,
                                    onItemSelected = { coordinate ->
                                        onItemSelected(coordinate)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun SearchBarCard(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .defaultMinSize(minHeight = 54.dp, minWidth = 132.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                contentDescription = "Search Icon"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Search",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun SavedLocationItem(
    modifier: Modifier = Modifier,
    data: ManageLocationsData,
    inSelectionMode: Boolean,
    selected: Boolean,
    onItemSelected: (Coordinate) -> Unit,
) {
    val transition = updateTransition(targetState = inSelectionMode)
    val itemHorizontalPadding by transition.animateDp(label = "item padding") { inEditMode ->
        if (inEditMode) 32.dp else 0.dp
    }
    val textSize by transition.animateFloat(label = "text size") { inEditMode ->
        if (inEditMode) 9f else 12f
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)) then modifier,
        shape = RoundedCornerShape(16.dp),
        border =
        if (data.isFavorite)
            BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = inSelectionMode,
                modifier = Modifier,
                enter = fadeIn(animationSpec = tween(25)),
                exit = fadeOut(animationSpec = tween(25)),
                label = "selection button"
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = "selected Icon",
                    tint =
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = itemHorizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = data.locationName,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(14.dp),
                            imageVector = Icons.Outlined.WaterDrop,
                            contentDescription = "Humidity Icon"
                        )
                        Text(
                            text = "${data.humidity}%",
                            fontSize = textSize.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Real Feel: ${
                                data.feelsLike
                            }°",
                            fontSize = textSize.sp
                        )
                    }
                }
                Text(
                    text = "${data.currentTemp}°",
                    fontSize = 28.sp
                )
            }
        }
    }
}


@ExperimentalFoundationApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ManageLocationsPreview() {
    val data = LocationsUIState.Success(
        listOf(
            ManageLocationsData(
                locationName = "Tehran",
                latitude = 10.toString(),
                longitude = 10.toString(),
                currentTemp = "2",
                humidity = "46",
                feelsLike = "1"
            ),
            ManageLocationsData(
                locationName = "Tabriz",
                latitude = 10.toString(),
                longitude = 10.toString(),
                currentTemp = "0",
                humidity = "55",
                feelsLike = "0"
            )
        )
    )
    WeatherTheme {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            ManageLocations(
                dataState = data,
                onNavigateToSearch = {},
                onBackPressed = {},
                onItemSelected = {},
                onDeleteItem = {},
                onSetFavoriteItem = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    WeatherTheme {
        CustomTopBar(text = "text", onBackPressed = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchCardPreview() {
    WeatherTheme {
        SearchBarCard(onClick = {})
    }
}

@Preview(showBackground = false)
@Composable
fun CityItemPreview() {
    WeatherTheme {
        SavedLocationItem(
            data = ManageLocationsData(
                locationName = "Tehran",
                latitude = 10.toString(),
                longitude = 10.toString(),
                currentTemp = "2",
                humidity = "46",
                feelsLike = "1"
            ),
            onItemSelected = {},
            inSelectionMode = true,
            selected = false
        )
    }
}
