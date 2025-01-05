package com.weather.feature.search

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.experiment.weather.core.common.R
import com.weather.core.design.components.weatherPlaceholder
import com.weather.core.design.modifiers.bouncyTapEffect
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.DailyPreview
import com.weather.model.geocode.GeoSearchItem
import com.weather.model.geocode.SavableSearchState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun SearchRoute(
    searchViewModel: SearchViewModel = hiltViewModel(),
    shouldRequestFocus: Boolean = true,
    onSelectSearchItem: () -> Unit,
) {
    //stateful
    val searchUIState by searchViewModel.searchUIState.collectAsStateWithLifecycle()
    val weatherPreview by searchViewModel.weatherPreview.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var inputText by remember {
        mutableStateOf(TextFieldValue(""))
    }
    LaunchedEffect(key1 = inputText) {
        searchViewModel.setSearchQuery(cityName = inputText.text.trim())
    }
    LaunchedEffect(key1 = searchUIState) {
        searchUIState.geoSearchItems[0].name?.let {
//            searchViewModel.getFiveDayPreview(searchUIState.geoSearchItems[0])
        }
    }
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SearchScreenContent(
                modifier = Modifier.padding(horizontal = 16.dp),
                searchUIState = searchUIState,
                weatherPreview = weatherPreview,
                shouldRequestFocus = shouldRequestFocus,
                searchInputText = inputText,
                popularCities = stringArrayResource(id = R.array.popular_cities).toList(),
                popularCityIndex = { cityIndex ->
                    val value = context.resources.getStringArray(R.array.popular_cities)[cityIndex]
                    inputText = TextFieldValue(
                        text = value,
                        selection = TextRange(value.length)
                    )
                },
                onSearchTextChange = { cityName ->
                    inputText = cityName
                },
                onClearSearch = {
                    inputText = TextFieldValue(text = "")
                }
            ) { searchItem ->
                searchViewModel.syncWeather(searchItem)
                onSelectSearchItem()
            }
        }
    }
}

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    searchUIState: SavableSearchState,
    weatherPreview: List<DailyPreview>,
    shouldRequestFocus: Boolean = true,
    searchInputText: TextFieldValue,
    popularCities: List<String>,
    popularCityIndex: (Int) -> Unit,
    onSearchTextChange: (TextFieldValue) -> Unit,
    onClearSearch: () -> Unit,
    selectedSearchItem: (GeoSearchItem) -> Unit,
) {
    //stateless
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopSearchBar(
            modifier = Modifier.padding(top = 16.dp),
            searchText = searchInputText,
            shouldRequestFocus = shouldRequestFocus,
            onTextChange = {
                onSearchTextChange(it)
            },
            onClearSearch = {
                onClearSearch()
            }
        )
        AnimatedContent(
            targetState = searchInputText.text.isEmpty(),
            label = "search content"
        ) { isTextSearchEmpty ->
            Column {
                if (isTextSearchEmpty) {
                    Text(
                        text = stringResource(id = R.string.popular_cities),
                        modifier = Modifier.padding(top = 24.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PopularCities(
                        modifier = Modifier,
                        popularCities = popularCities,
                        popularCityIndex = { popularCityIndex(it) })

                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchList(
                        searchList = searchUIState.geoSearchItems,
                        showPlaceholder = searchUIState.showPlaceholder,
                        weatherPreview = weatherPreview,
                        onSearchItemSelected = { searchItem ->
                            selectedSearchItem(searchItem)
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
    searchText: TextFieldValue,
    shouldRequestFocus: Boolean,
    onTextChange: (TextFieldValue) -> Unit,
    onClearSearch: () -> Unit,
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    )
    val focusRequester = remember {
        FocusRequester()
    }
    if (shouldRequestFocus) {
        LaunchedEffect(key1 = Unit) {
            focusRequester.requestFocus()
        }
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
            placeholder = {
                Text(
                    text = "Search",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentDescription = "Search Icon"
                )

            },
            trailingIcon = {
                if (searchText.text.isNotEmpty()) {
                    TextButton(
                        onClick = { onClearSearch() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.DarkGray
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
    weatherPreview: List<DailyPreview>?,
    showPlaceholder: Boolean,
    onSearchItemSelected: (GeoSearchItem) -> Unit,
) {
    //max 5 item per search list
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        itemsIndexed(searchList) { index, searchItemItem ->
            if(!weatherPreview.isNullOrEmpty()) {
                if (index == 1) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FiveDaySearchPreview(weatherPreview = weatherPreview)
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(0.9f)
                        )
                    }
                }
            }
            SearchItem(
                modifier = Modifier
                    .bouncyTapEffect()
                    .clip(RoundedCornerShape(16.dp))
                    .weatherPlaceholder(
                        visible = showPlaceholder
                    )
                    .clickable { onSearchItemSelected(searchItemItem) },
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
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
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PopularCities(
    modifier: Modifier = Modifier,
    popularCities: List<String>,
    popularCityIndex: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth() then modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        popularCities.forEachIndexed { index, cityName ->
            PopularCityItem(
                modifier = Modifier
                    .bouncyTapEffect(targetScale = 0.9f)
                    .clip(shape = RoundedCornerShape(size = 16.dp))
                    .clickable { popularCityIndex(index) },
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
        modifier = Modifier then modifier,
        shape = RoundedCornerShape(size = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )
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
            mutableStateOf(TextFieldValue(text = "Tehran"))
        }
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            SearchScreenContent(
                searchUIState = SavableSearchState(GeoSearchItem.empty, true),
                weatherPreview = dailyDummyData,
                searchInputText = inputText,
                shouldRequestFocus = false,
                popularCities = popularCities,
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
        TopSearchBar(
            Modifier,
            searchText = TextFieldValue(""),
            onTextChange = {},
            onClearSearch = {},
            shouldRequestFocus = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PopularCityPreview() {
    WeatherTheme {
        PopularCities(
            popularCities = popularCities,
            popularCityIndex = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchItemPreview() {
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

val popularCities = listOf(
    "Tehran",
    "Beijing",
    "Hong Kong",
    "Rome",
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
)

//internal val items = listOf(
//    GeoSearchItem("Iran", name = "Tehran"),
//    GeoSearchItem("Iran2", name = "Tehran2"),
//    GeoSearchItem("Iran3", name = "Tehran3"),
//    GeoSearchItem("Iran4", name = "Tehran4"),
//    GeoSearchItem("Iran5", name = "Tehran5"),
//)