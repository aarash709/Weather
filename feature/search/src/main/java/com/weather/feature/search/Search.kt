package com.weather.feature.search

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.weather.core.design.components.LinearLoadingIndicator
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSelectSearchItem: () -> Unit,
) {
    //stateful
    val searchUIState by searchViewModel.searchUIState.collectAsStateWithLifecycle()
    var inputText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = inputText){
        searchViewModel.setSearchQuery(cityName = inputText)
    }
    Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
//                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            //        Text(text = "selected Item is: $selectedItem", color = Color.Red) //debug
            SearchScreen(
                searchUIState = searchUIState,
                searchInputText = inputText,
                popularCities = cityList,
                popularCityIndex = { inputText = cityList[it] },
                onSearchTextChange = { cityName ->
                    inputText = cityName.ifBlank {
                        cityName.trim()
                    }

                },
                onClearSearch = {
                    inputText = ""
                },
                selectedSearchItem = { searchItem ->
//                    searchViewModel.saveSearchWeatherItem(searchItem)
                    searchViewModel.syncWeather(searchItem)
                    onSelectSearchItem()
                }
            )
        }
    }
}

@Composable
fun SearchScreen(
    searchUIState: SearchUIState,
    searchInputText: String,
    popularCities: List<String>,
    popularCityIndex: (Int) -> Unit,
    onSearchTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    selectedSearchItem: (GeoSearchItem) -> Unit,
) {
    //stateless
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopSearchBar(
            modifier = Modifier.padding(top = 16.dp),
            searchText = searchInputText,
            onTextChange = {
                onSearchTextChange(it)
            },
            onClearSearch = {
                onClearSearch()
            }
        )
        if (searchInputText.isEmpty()) {
            Text(
                text = "Popular Cities",
                modifier = Modifier.padding(top = 24.dp),
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            PopularCities(
                popularCities = popularCities,
                popularCityIndex = { popularCityIndex(it) })

        } else {
            when (searchUIState) {
                is SearchUIState.Loading -> LinearLoadingIndicator(
                    modifier = Modifier.padding(
                        horizontal = 24.dp
                    )
                )
                is SearchUIState.Error -> Unit
                is SearchUIState.Success -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchList(
                        searchList = searchUIState.data,
                        onSearchItemSelected = { SearchItem ->
                            selectedSearchItem(SearchItem)
                            //fetch and store weather based on selection
                            //maybe navigate to main page after successful IO
                        })
                }
            }

        }
    }
}


@Composable
private fun TopSearchBar(
    modifier: Modifier,
    searchText: String,
    onTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
) {
    val textFieldColors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onSurface,
        backgroundColor = MaterialTheme.colors.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    )
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = searchText,
            onValueChange = { onTextChange(it) },
            modifier = Modifier
                .focusRequester(focusRequester)
                .weight(weight = 1f, fill = true),
            placeholder = { Text(text = "Search") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentDescription = "Search Icon"
                )

            },
            trailingIcon = {
                if (searchText.isNotBlank()) {
                    TextButton(
                        onClick = { onClearSearch() },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.DarkGray,
                            backgroundColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Microphone Icon"
                        )
                    }
                }
            },
            shape = CircleShape,
            colors = textFieldColors,
        )

    }
}

//search results
@Composable
private fun SearchList(
    searchList: List<GeoSearchItem>,
    onSearchItemSelected: (GeoSearchItem) -> Unit,
) {
    //max 5 item per search list
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(searchList) { searchItemItem ->
            SearchItem(
                modifier = Modifier.clickable { onSearchItemSelected(searchItemItem) },
                item = searchItemItem
            )
        }
    }
}

@Composable
private fun SearchItem(
    modifier: Modifier = Modifier,
    item: GeoSearchItem,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.name.toString(),
                    fontSize = 14.sp
                )
                Text(
                    text = "${item.name}, ${item.country}",
                    fontSize = 10.sp,
                )
            }
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
        }
    }
}

//popular city
@Composable
private fun PopularCities(
    popularCities: List<String>,
    popularCityIndex: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(fraction = 1f),
//        mainAxisSize = SizeMode.Wrap, //buggy
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSpacing = 16.dp,
        crossAxisAlignment = FlowCrossAxisAlignment.Center,
        crossAxisSpacing = 16.dp,
    ) {
        popularCities.forEachIndexed { index, cityName ->
            PopularCityItem(
                modifier = Modifier.clickable { popularCityIndex(index) },
                cityName = cityName
            )
        }
    }

}

@Composable
private fun PopularCityItem(
    modifier: Modifier = Modifier,
    cityName: String,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 16.dp),
    ) {
        Text(
            text = cityName,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        )
    }

}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SearchPreview() {
    WeatherTheme {
        var inputText by remember {
            mutableStateOf("input text")
        }
        Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
            SearchScreen(
                searchUIState = SearchUIState.Loading,
                searchInputText = "",
                popularCities = cityList,
                popularCityIndex = {},
                onClearSearch = {},
                onSearchTextChange = { inputText = it },
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopSearchBarPreview() {
    WeatherTheme {
        TopSearchBar(Modifier, searchText = "", onTextChange = {}, onClearSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PopularCityPreview() {
    WeatherTheme {
        PopularCities(
            popularCities = cityList,
            popularCityIndex = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchItemPreview() {
    WeatherTheme {
        val item = GeoSearchItem("Iran", name = "Tehran")
        SearchItem(Modifier, item)
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchCityWeatherPreview() {
    WeatherTheme {
    }
}

val cityList = listOf(
    "Tehran",
    "Beijing",
    "Hong Kong",
    "Rome",
    "Shang Hai",
    "Shang Hai",
    "Moscow",
    "Los Angeles",
    "Chicago",
    "Bangkok",
    "Seoul",
    "Kuala Lumpur",
    "New York",
    "Munich",
    "Boston",
    "Singapore",
    "Shiraz",
    "Berlin",
    "City"
)

val items = listOf(
    GeoSearchItem("Iran", name = "Tehran"),
    GeoSearchItem("Iran2", name = "Tehran2"),
    GeoSearchItem("Iran3", name = "Tehran3"),
    GeoSearchItem("Iran4", name = "Tehran4"),
    GeoSearchItem("Iran5", name = "Tehran5"),
)