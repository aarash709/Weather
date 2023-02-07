package com.experiment.weather.presentation.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.experiment.weather.presentation.ui.components.common.ShowLoading
import com.experiment.weather.presentation.viewmodel.LocationsUIState
import com.experiment.weather.presentation.viewmodel.ManageLocationsViewModel
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.ManageLocationsData
import kotlin.math.roundToInt

@Composable
fun ManageLocations(
    viewModel: ManageLocationsViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
) {
    //stateful
    val dataState by viewModel.locationsState.collectAsStateWithLifecycle()
    Surface {
        ManageLocations(
            dataState = dataState,
            onNavigateToSearch = { onNavigateToSearch() },
            onBackPressed = onBackPressed,
            onItemSelected = { cityName ->
                viewModel.saveFavoriteCity(cityName = cityName)
                onItemSelected(cityName)
            }
        )
    }
}

@Composable
fun ManageLocations(
    dataState: LocationsUIState,
    onNavigateToSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
) {
    //stateless
    when (dataState) {
        is LocationsUIState.Loading -> ShowLoading()
        is LocationsUIState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 0.dp
                ) {
                    TopBar(
                        onBackPressed = onBackPressed
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SearchBarCard(onNavigateToSearch)
                Spacer(modifier = Modifier.height(16.dp))
                FavoriteLocations(
                    dataList = dataState.data,
                    onItemSelected = { cityName ->
                        onItemSelected(cityName)
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
        backgroundColor = Color.LightGray,
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
                tint = Color.DarkGray,
                contentDescription = "Search Icon"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Search",
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun FavoriteLocations(
    dataList: List<ManageLocationsData>,
    onItemSelected: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(dataList) { locationData ->
            SavedLocationItem(
                data = locationData,
                onItemSelected = { cityName ->
                    onItemSelected(cityName)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedLocationItem(
    data: ManageLocationsData,
    onItemSelected: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.LightGray,
        onClick = { onItemSelected(data.locationName) }
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
                            data.feelsLike.toFloat().minus(273.15f).roundToInt()}°",
                        fontSize = 12.sp
                    )
//                    Text(text = "30°/20°")
                }
            }
            Text(
                text = "${data.currentTemp.toFloat().minus(273.15f).roundToInt()}°",
                fontSize = 28.sp
            )
        }
    }
}

@Composable
private fun TopBar(
    onBackPressed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Icon"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Manage Locations",
            fontSize = 18.sp
        )
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

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    WeatherTheme {
        TopBar(onBackPressed = {})
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
                currentTemp = "2",
                humidity = "46",
                feelsLike = "1"
            ),
            onItemSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ManageLocationsPreview() {
    val data = LocationsUIState.Success(
        listOf(
            ManageLocationsData(
                locationName = "Tehran",
                currentTemp = "2",
                humidity = "46",
                feelsLike = "1"
            ),
            ManageLocationsData(
                locationName = "Tabriz",
                currentTemp = "0",
                humidity = "55",
                feelsLike = "0"
            )
        )
    )
    WeatherTheme {
        ManageLocations(
            dataState = data,
            onNavigateToSearch = {},
            onBackPressed = {}
        ) {}
    }
}