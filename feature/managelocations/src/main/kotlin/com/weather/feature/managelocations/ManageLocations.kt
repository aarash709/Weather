package com.weather.feature.managelocations

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.weather.core.design.components.CustomTopBar
import com.weather.core.design.components.ShowLoadingText
import com.weather.core.design.modifiers.bouncyTapEffect
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.managelocations.components.LocationsTopbar
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData

@ExperimentalFoundationApi
@Composable
fun ManageLocationsRoute(
    viewModel: ManageLocationsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    //stateful
    val dataState by viewModel.locationsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        ManageLocations(
            dataState = dataState,
            onBackPressed = onBackPressed,
            onItemSelected = { coordinate ->
                onItemSelected(coordinate.cityName.toString())
            },
            onDeleteItem = { cityNames ->
                viewModel.deleteWeatherByCityName(cityNames = cityNames, context = context)
            },
            onSetFavoriteItem = { favoriteCity ->
                viewModel.saveFavoriteCityCoordinate(cityName = favoriteCity, context = context)
            },
            onNavigateToSearch = { onNavigateToSearch() }
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun ManageLocations(
    dataState: LocationsUIState,
    onBackPressed: () -> Unit,
    onItemSelected: (Coordinate) -> Unit,
    onDeleteItem: (List<String>) -> Unit,
    onSetFavoriteItem: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
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
    var itemsToDelete by remember {
        mutableStateOf(listOf<String>())
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
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
            LocationsTopbar(
                isInEditMode = isInEditMode,
                selectedCitySize = selectedCities.size,
                scrollBehavior = scrollBehavior,
                onBackPressed = { onBackPressed() },
                onIsAllSelected = { isAllSelected = !isAllSelected },
                onEmptyCitySelection = { selectedCities = emptySet() }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isInEditMode,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                label = "bottom bar content"
            ) {
                BottomAppBar(
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BottomBarItem(
                            buttonName = "Delete",
                            imageVector = Icons.Default.DeleteOutline,
                            onClick = {
                                onDeleteItem(itemsToDelete)
                                selectedCities = emptySet()
                            })
                        AnimatedVisibility(selectedCities.size < 2) {
                            BottomBarItem(
                                buttonName = "Favorite",
                                imageVector = Icons.Default.StarBorder,
                                onClick = {
                                    onSetFavoriteItem(selectedCities.first())
                                })
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
                    SearchBarCard(onClick = {
                        onNavigateToSearch()
                    })
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
//                                    onItemSelected = { coordinate ->
//                                        onItemSelected(coordinate)
//                                    }
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
fun BottomBarItem(
    modifier: Modifier = Modifier,
    buttonName: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { onClick() }) {
            Icon(
                imageVector = imageVector,
                modifier = Modifier.size(28.dp),
                contentDescription = "Delete button"
            )
        }
        Text(text = buttonName, fontSize = 14.sp)
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
//    onItemSelected: (Coordinate) -> Unit,
) {
    val transition = updateTransition(targetState = inSelectionMode, label = "selection mode")
    val itemHorizontalPadding by transition.animateDp(label = "item padding") { inEditMode ->
        if (inEditMode) 12.dp else 0.dp
    }
    val isFavorite = data.isFavorite
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)) then modifier,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(
                visible = inSelectionMode,
                modifier = Modifier,
                enter = fadeIn(animationSpec = tween(25)) + expandHorizontally(),
                exit = fadeOut(animationSpec = tween(25)) + shrinkHorizontally(),
                label = "selection button"
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    contentDescription = "selected Icon",
                    tint =
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = data.locationName,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    if (isFavorite) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Yellow,
                            contentDescription = "selected icon star"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(14.dp),
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = "Humidity Icon"
                    )
                    Text(
                        text = "${data.humidity}%",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Real Feel: ${
                            data.feelsLike
                        }°",
                        fontSize = 12.sp
                    )
                }
            }
            Row(
                modifier = Modifier.padding(horizontal = itemHorizontalPadding),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${data.weatherIcon}@2x.png",
                    modifier = Modifier,
                    contentDescription = "weather icon"
                )
                Text(
                    text = "${data.currentTemp}°",
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.End,
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
                weatherIcon = "",
                latitude = 10.toString(),
                longitude = 10.toString(),
                currentTemp = "2",
                humidity = "46",
                feelsLike = "1"
            ),
            ManageLocationsData(
                locationName = "Tabriz",
                weatherIcon = "",
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
                onBackPressed = {},
                onItemSelected = {},
                onDeleteItem = {},
                onSetFavoriteItem = {},
                onNavigateToSearch = {})
        }
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
                weatherIcon = "",
                latitude = 10.toString(),
                longitude = 10.toString(),
                currentTemp = "2",
                humidity = "46",
                feelsLike = "1"
            ),
            inSelectionMode = true,
            selected = false
        )
    }
}
