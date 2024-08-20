package com.weather.feature.managelocations

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.experiment.weather.core.common.R
import com.weather.core.design.components.ShowLoadingText
import com.weather.core.design.modifiers.bouncyTapEffect
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.managelocations.components.LocationsBottomBar
import com.weather.feature.managelocations.components.LocationsTopbar
import com.weather.feature.managelocations.components.SearchBarCard
import com.weather.feature.managelocations.components.detectLongPressGesture
import com.weather.feature.managelocations.components.draggableItem
import com.weather.feature.managelocations.components.locationsClickable
import com.weather.feature.managelocations.components.rememberDragAndDropListItem
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
    var shouldSelectAll by rememberSaveable {
        mutableStateOf(false)
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(key1 = shouldSelectAll) {
        if (shouldSelectAll) {
            selectedCities += locationsList.map { it.locationName }
        } else {
            selectedCities = emptySet()
        }
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
                onIsAllSelected = { shouldSelectAll = !shouldSelectAll },
                onEmptyCitySelection = { selectedCities = emptySet() }
            )
        },
        bottomBar = {
            LocationsBottomBar(
                isInEditMode = isInEditMode,
                selectedCitySize = selectedCities.size,
                onDeleteItem = { onDeleteItem(selectedCities.toList()) },
                onEmptyCitySelection = { selectedCities = emptySet() },
                onSetFavoriteItem = {
                    onSetFavoriteItem(selectedCities.first())
                })
        }
    ) { padding ->
        when (dataState) {
            is LocationsUIState.Loading -> ShowLoadingText()
            is LocationsUIState.Success -> {
                LaunchedEffect(key1 = dataState) {
                    locationsList = dataState.data
                    if (locationsList.size == 1) {
                        onSetFavoriteItem(locationsList.first().locationName)
                    }
                }
                Column(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
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
                            text = stringResource(id = R.string.search_and_add_a_location),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .75f),
                            fontSize = 12.sp
                        )
                    } else {
                        val lazyListState = rememberLazyListState()
                        var list by remember {
                            mutableStateOf(dataState.data)
                        }
                        val dragDropState =
                            rememberDragAndDropListItem(
                                lazyListState = lazyListState,
                                onUpdateData = { fromIndex, toIndex ->
                                    list = list
                                        .toMutableList()
                                        .apply {
                                            add(
                                                toIndex,
                                                removeAt(fromIndex)
                                            )
                                        }
                                })
                        LazyColumn(
                            modifier = Modifier
                                .detectLongPressGesture(
                                    lazyListState = lazyListState,
                                    dragAndDropListItemState = dragDropState
                                ),
                            state = lazyListState,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            itemsIndexed(
                                items = list,
                                key = { _, item ->
                                    item.locationName
                                }) { index, locationData ->
                                val selected by remember(selectedCities) {
                                    mutableStateOf(locationData.locationName in selectedCities)
                                }
                                SavedLocationItem(
                                    modifier = Modifier
                                        .bouncyTapEffect()
                                        .locationsClickable(
                                            inSelectionMode = inSelectionMode,
                                            onSelectionMode = {
                                                if (selected)
                                                    selectedCities -= locationData.locationName
                                                else
                                                    selectedCities += locationData.locationName
                                            },
                                            onItemSelected = {
                                                onItemSelected(
                                                    Coordinate(
                                                        locationData.locationName,
                                                        locationData.latitude,
                                                        locationData.longitude
                                                    )
                                                )
                                            },
                                            onLongClick = { selectedCities += locationData.locationName }
                                        )
                                        .draggableItem(
                                            draggableState = dragDropState,
                                            listIndex = index
                                        )
                                            then if (dragDropState.draggableItemIndex != index) {
                                        Modifier.animateItem()
                                    } else {
                                        Modifier
                                    },
                                    data = locationData,
                                    inSelectionMode = inSelectionMode,
                                    selected = selected,
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
internal fun SavedLocationItem(
    modifier: Modifier = Modifier,
    data: ManageLocationsData,
    inSelectionMode: Boolean,
    selected: Boolean,
) {
    val isFavorite = data.isFavorite
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(
                visible = inSelectionMode,
                modifier = Modifier,
                enter = fadeIn(animationSpec = tween(100)) + expandHorizontally(animationSpec = spring()),
                exit = fadeOut(animationSpec = tween(25)) + shrinkHorizontally(),
                label = "draggable icon"
            ) {
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    modifier = Modifier
                        .padding(end = 16.dp),
                    contentDescription = "draggable icon"
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
            }
            Row(
                modifier = Modifier,
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
            AnimatedVisibility(
                visible = inSelectionMode,
                modifier = Modifier,
                enter = fadeIn(animationSpec = tween(100)) + expandHorizontally(animationSpec = spring()),
                exit = fadeOut(animationSpec = tween(25)) + shrinkHorizontally(),
                label = "selection button"
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    modifier = Modifier
                        .padding(start = 16.dp),
                    contentDescription = "selected Icon",
                    tint =
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
        }
    }
}


@ExperimentalFoundationApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
internal fun ManageLocationsPreview() {
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
internal fun SearchCardPreview() {
    WeatherTheme {
        SearchBarCard(onClick = {})
    }
}

@Preview(showBackground = false)
@Composable
internal fun CityItemPreview() {
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
