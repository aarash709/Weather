package com.weather.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.flowlayout.*
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.geocode.GeoSearchItem
import kotlinx.coroutines.FlowPreview

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

    var selectedItem by remember {
        mutableStateOf("")
    }
//    val saved by searchViewModel.saved
    val size = LocalConfiguration.current.screenWidthDp

    Surface {
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
                onSearchTextChange = {
                    inputText = it.trim()
                    searchViewModel.searchCity(it)
                },
                onClearSearch = {
                    inputText = ""
                },
                selectedSearchItem = { searchItem ->
                    selectedItem = searchItem.name + searchItem.country //debug
                    searchViewModel.saveSearchWeatherItem(searchItem)

                    //                if (saved)
                    onSelectSearchItem()
                    // sending the city name as arg then fetch related data
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
    onSearchTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    selectedSearchItem: (GeoSearchItem) -> Unit,
) {
    //stateless
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopSearchBar(
            searchText = searchInputText,
            onTextChange = {
                onSearchTextChange(it)
            },
            onClearSearch = {
                onClearSearch()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (searchUIState) {
            is SearchUIState.Success -> {
                SearchItems(
                    searchList = searchUIState.data,
                    onSearchItemSelected = { SearchItem ->
                        selectedSearchItem(SearchItem)
                        //fetch and store weather based on selection
                        //maybe navigate to main page after successful IO
                    })
            }
            is SearchUIState.Loading -> {
                if (searchInputText.isEmpty()) {
                    Text(
                        text = "Popular Cities",
                        modifier = Modifier.padding(top = 8.dp),
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PopularCities(popularCities = popularCities)
                } else {
                    ShowLoading()
                }
            }
            is SearchUIState.Error -> {
                //inform user of network problems
                //network error NOT implemented yet
                //should implement a network monitor
            }
        }
    }
}


@Composable
private fun TopSearchBar(
    searchText: String,
    onTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
) {
    val textFieldColors = TextFieldDefaults.textFieldColors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        backgroundColor = Color.LightGray
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = searchText,
            onValueChange = { onTextChange(it) },
            modifier = Modifier
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
                if (searchText.isNotBlank()){
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

//@Composable
//private fun ForecastPreview(
//    geoSearchItems: List<GeoSearchItem>,
//    onSearchItemSelected: (GeoSearchItem) -> Unit,
//) {
//    Column {
//        SearchCityName()
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .padding(end = 8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            WeatherForecastItemPreview()
//            WeatherForecastItemPreview()
//            WeatherForecastItemPreview()
//            WeatherForecastItemPreview()
//            WeatherForecastItemPreview()
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        SearchItems(
//            geoSearchItems,
//            onSearchItemSelected = { onSearchItemSelected(it) }
//        )
//    }
//}

@Composable
fun SearchCityName() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(.8f)) {
                Text(
                    text = "London",
                    fontSize = 14.sp
                )
                Text(
                    text = "London, England",
                    fontSize = 8.sp,
                    color = Color.LightGray
                )
            }
            Icon(

                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add Icon"
            )
        }
    }

}


@Composable
private fun WeatherForecastItemPreview() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Today",
            fontSize = 12.sp
        )
        Text(
            text = "27 Sep",
            fontSize = 8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Icon(imageVector = Icons.Default.Cloud, contentDescription = "Weather Icon")
        Text(
            text = "32°",
            color = Color.LightGray,
            fontSize = 10.sp
        )
    }
}

//search results
@Composable
private fun SearchItems(
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
        backgroundColor = Color.LightGray
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
                    color = Color.DarkGray
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
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(fraction = 1f),
//        mainAxisSize = SizeMode.Wrap, //buggy
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSpacing = 16.dp,
        crossAxisAlignment = FlowCrossAxisAlignment.Center,
        crossAxisSpacing = 16.dp,
    ) {
        popularCities.forEach {
            PopularCityItem(cityName = it)
        }
    }

}

@Composable
private fun PopularCityItem(
    cityName: String,
) {
    Box(modifier = Modifier) {
        Surface(
            modifier = Modifier,
            shape = RoundedCornerShape(size = 16.dp),
            color = Color.LightGray
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
}


@Preview(showBackground = true)
@Composable
private fun SearchPreview() {
    WeatherTheme {
        var inputText by remember {
            mutableStateOf("input text")
        }
        SearchScreen(
            searchUIState = SearchUIState.Loading,
            searchInputText = "",
            popularCities = cityList,
            onClearSearch = {},
            onSearchTextChange = { inputText = it },
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun TopSearchBarPreview() {
    WeatherTheme {
        TopSearchBar(searchText = "", onTextChange = {}, onClearSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PopularCityPreview() {
    WeatherTheme {
        PopularCities(
            popularCities = cityList,
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