package com.weather.feature.managelocations

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weather.core.design.components.CustomTopBar
import com.weather.core.design.components.ShowLoadingText
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData
import kotlin.math.roundToInt

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ManageLocations(
    viewModel: ManageLocationsViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
) {
    //stateful
    val dataState by viewModel.locationsState.collectAsStateWithLifecycle()
    Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
        ManageLocations(
            dataState = dataState,
            onNavigateToSearch = { onNavigateToSearch() },
            onBackPressed = onBackPressed,
            onItemSelected = { coordinate ->
                viewModel.saveFavoriteCityCoordinate(coordinate)
                onItemSelected(coordinate.cityName.toString())
            },
            onDeleteItem = { locationData ->
                val cityName = locationData.locationName
                viewModel.deleteWeatherByCityName(cityName = cityName)
            },
            onSetFavoriteItem = { locationData ->
                val coordinate = Coordinate(
                    cityName = locationData.locationName,
                    latitude = locationData.latitude,
                    longitude = locationData.longitude
                )
                viewModel.saveFavoriteCityCoordinate(coordinate)
            }
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ManageLocations(
    dataState: LocationsUIState,
    onNavigateToSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onItemSelected: (Coordinate) -> Unit,
    onDeleteItem: (ManageLocationsData) -> Unit,
    onSetFavoriteItem: (ManageLocationsData) -> Unit,
) {
    //stateless
    when (dataState) {
        is LocationsUIState.Loading -> ShowLoadingText()
        is LocationsUIState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CustomTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Manage Locations"
                ) {
                    onBackPressed()
                }
                Spacer(modifier = Modifier.height(16.dp))
                SearchBarCard(onNavigateToSearch)
                Spacer(modifier = Modifier.height(16.dp))
                FavoriteLocations(
                    dataList = dataState.data,
                    onItemSelected = { coordinate ->
                        onItemSelected(coordinate)
                    },
                    onDeleteItem = { locationData ->
                        onDeleteItem(locationData)
                    },
                    onSetFavoriteItem = { locationsData ->
                        onSetFavoriteItem(locationsData)
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBarCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        elevation = 0.dp,
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
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                contentDescription = "Search Icon"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Search",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun FavoriteLocations(
    dataList: List<ManageLocationsData>,
    onItemSelected: (Coordinate) -> Unit,
    onDeleteItem: (ManageLocationsData) -> Unit,
    onSetFavoriteItem: (ManageLocationsData) -> Unit,
) {
    LazyColumn(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(dataList, key = {
            it.locationName
        }) { locationData ->
            val currentItem by rememberUpdatedState(newValue = locationData)
            CustomSwipeDismiss(
                modifier = Modifier.animateItemPlacement(),
                dismissThreshold = 0.5f,
                onDeleteItem = { onDeleteItem(currentItem) },
                onSetFavoriteItem = { onSetFavoriteItem(currentItem) }
            ) {
                SavedLocationItem(
                    data = locationData,
                    onItemSelected = { coordinate ->
                        onItemSelected(coordinate)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedLocationItem(
    data: ManageLocationsData,
    onItemSelected: (Coordinate) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = if (data.isFavorite) BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary
        ) else null,
        onClick = { onItemSelected(Coordinate(data.locationName, data.latitude, data.longitude)) }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.Start) {
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
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Real Feel: ${
                            data.feelsLike}°",
                        fontSize = 12.sp
                    )
//                    Text(text = "30°/20°")
                }
            }
            Text(
                text = "${data.currentTemp}°",
                fontSize = 28.sp
            )
        }
    }
}

@Composable
fun SearchBar(
    textFieldColors: TextFieldColors,
) {
    var textValue: String by remember {
        mutableStateOf("")
    }

    TextField(
        value = textValue,
        onValueChange = { textValue = it },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text(text = "Search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        shape = RoundedCornerShape(32.dp),
        colors = textFieldColors
    )
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
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
        Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
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
        CustomTopBar(text = "text",onBackPressed = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchCardPreview() {
    WeatherTheme {
        SearchBarCard(onClick = {})
    }
}

@Preview(showBackground = true)
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
            onItemSelected = {}
        )
    }
}
