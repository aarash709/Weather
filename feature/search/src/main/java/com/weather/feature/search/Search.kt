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
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.geocode.GeoSearchItem

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
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            //        Text(text = "selected Item is: $selectedItem", color = Color.Red) //debug
            SearchScreen(
                searchUIState = searchUIState,
                searchInputText = inputText,
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
    onSearchTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    selectedSearchItem: (GeoSearchItem)-> Unit
) {
    //stateless
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    onSearchItemSelected = {SearchItem->
                        selectedSearchItem(SearchItem)
                        //fetch and store weather based on selection
                        //maybe navigate to main page after successful IO
                    })
            }
            is SearchUIState.Loading -> {
                if(searchInputText.isEmpty()){
                    PopularCities()
                }else{
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
                .weight(weight = 1f, fill = true)
                .padding(horizontal = 8.dp),
            placeholder = { Text(text = "Enter Location") },
            leadingIcon = {
                TextButton(
                    onClick = {
                        onClearSearch()
                        /*TODO navigate back to prev page*/
                    },
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Microphone Icon"
                )
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
    Card(shape = RoundedCornerShape(16.dp), backgroundColor = Color.LightGray) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
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
}

//popular city
@Composable
private fun PopularCities() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Popular Cities",
            color = Color.DarkGray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
//    val list = listOf("tehran",
//        "london",
//        "New York",
//        "Munich",
//        "neeeee",
//        "Shiraz",
//        "anothercityname",
//        "city")
//    LazyVerticalGrid(columns = GridCells.Adaptive(128.dp)) {
//        items(list) {
//            PopularCityItem(cityName = it)
//        }
//
//    }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("tehran", "london", "New York"/*,"Munich"*/).forEach {
                PopularCityItem(cityName = it)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("Beijing", "Tokyo", "Singapore"/*,"Paris"*/).forEach {
                PopularCityItem(cityName = it)
            }
        }
    }

}

@Composable
private fun PopularCities(popularCities: List<String>) {
    LazyColumn() {
        items(popularCities.size) { index ->
            PopularCityItem(cityName = popularCities[index])
        }
    }
}

@Composable
private fun PopularCityItem(cityName: String) {
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
                    vertical = 4.dp
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
        val items = listOf(
            GeoSearchItem("Iran", name = "Tehran"),
            GeoSearchItem("Iran2", name = "Tehran2"),
            GeoSearchItem("Iran3", name = "Tehran3"),
            GeoSearchItem("Iran4", name = "Tehran4"),
            GeoSearchItem("Iran5", name = "Tehran5"),
        )
        SearchScreen(
            searchUIState = SearchUIState.Loading,
            searchInputText = "",
            onClearSearch = {},
            onSearchTextChange = { inputText = it }
        ) {

        }
//        SearchScreen(
//            searchUIState = SearchUIState.Loading,
//            searchInputText = inputText,
//            onSearchTextChange = {
//                inputText = it
//            },
//            onClearSearch = {
//                inputText = ""
//            }
//        )
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
        PopularCities()
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
        Text(text = "text")
//        HourlyDailyTemps()
    }
}