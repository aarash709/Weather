package com.weather.feature.forecast

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.Current
import com.weather.model.OneCallCoordinates
import com.weather.model.WeatherData
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun WeatherForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
    navigateToManageLocations: () -> Unit,
    navigateToOnboard: () -> Unit,
) {
    //stateful
    val databaseIsEmpty by viewModel.dataBaseOrCityIsEmpty.collectAsStateWithLifecycle()
    val weatherUIState by viewModel
        .weatherUIState.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = databaseIsEmpty
    ) {
        Timber.e("ui: $databaseIsEmpty")
    }
    if (databaseIsEmpty) {
        LaunchedEffect(key1 = Unit) {
            navigateToOnboard()
        }
    } else {
        Surface(modifier = Modifier.fillMaxSize()) {
            WeatherForecastScreen(
                weatherUIState = weatherUIState,
                onNavigateToManageLocations = { navigateToManageLocations() }
            )
        }
    }
}

@Composable
fun WeatherForecastScreen(
    weatherUIState: WeatherUIState,
    onNavigateToManageLocations: () -> Unit,
) {
    // stateless
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(
                rememberScrollState(),
                reverseScrolling = false
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        when (weatherUIState) {
            WeatherUIState.Loading -> ShowLoading()
            is WeatherUIState.Success -> {
                TopAppBar(
                    modifier = Modifier,
                    elevation = 0.dp
                ) {
                    TopBar(
                        cityName = weatherUIState.data.coordinates.name.toString(),
                        onNavigateToManageLocations = { onNavigateToManageLocations() }
                    )
                }
                CurrentWeather(
                    weatherData = weatherUIState.data.current
                )
                Text(text = "Daily")
                Daily(dailyList = weatherUIState.data.daily.map { it.toDailyPreview() })
                Text(text = "Today")
                HourlyForecast(
                    modifier = Modifier.padding(bottom = 16.dp),
                    data = weatherUIState.data.hourly
                )
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .size(size = 60.dp)
//                .background(Brush.horizontalGradient(listOf(Color.Red, Color.Yellow))),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "Sunset/Sunrise")
//        }
//        HourlyForecast()
//        Column(
//            modifier = Modifier,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            DailyForecastItem()
//            DailyForecastItem()
//            DailyForecastItem()
//            DailyForecastItem()
//            DailyForecastItem()
//        }
    }
}


@Composable
private fun TopBar(
    cityName: String,
    onNavigateToManageLocations: () -> Unit,
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
            modifier = Modifier.padding(8.dp),
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
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Location Picker Icon"
            )
        }
//        Switch(checked = false, onCheckedChange = {/*todo change theme*/ })
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Location Pick Icon"
        )
    }
}

@Composable
private fun CurrentWeather(
    weatherData: Current,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CurrentTempAndCondition(
                modifier = Modifier
                    .padding(vertical = 48.dp)
                    .fillMaxWidth(),
                icon = weatherData.weather[0].icon,
                temp = weatherData.temp.minus(273.15).roundToInt().toString(),
                feelsLikeTemp = weatherData.feels_like.minus(273.15).roundToInt().toString(),
                condition = weatherData.weather.first().main
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        CurrentWeatherDetails(
            weatherData = weatherData
        )
    }
}

@Composable
fun CurrentWeatherDetails(
    weatherData: Current,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherDetailItem(
            image = Icons.Outlined.Air,
            value = "${weatherData.wind_speed}km/h",
            itemName = "Wind Speed"
        )
        WeatherDetailItem(
            image = Icons.Outlined.WaterDrop,
            value = "${weatherData.humidity}%",
            itemName = "Humidity"
        )
        WeatherDetailItem(
            image = Icons.Outlined.Visibility,
            value = "${weatherData.visibility}km",
            itemName = "Visibility"
        )
    }
}

@Composable
private fun WeatherDetailItem(
    image: ImageVector,
    value: String,
    itemName: String,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                imageVector = image,
                contentDescription = itemName,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = value,
                fontSize = 10.sp
            )
        }
        Text(
            text = itemName,
            fontSize = 10.sp
        )
    }
}


@Composable
private fun CurrentTempAndCondition(
    modifier: Modifier = Modifier,
    icon: String,
    temp: String,
    feelsLikeTemp: String,
    condition: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
//        AsyncImage(
//            model = "https://openweathermap.org/img/wn/$icon@2x.png",
//            contentDescription = "WeatherIcon",
//            modifier = Modifier.size(120.dp)
//        )
//        Image(
//            imageVector = Icons.Outlined.WbCloudy,
//            contentDescription = "Current Weather",
//            modifier = Modifier.size(128.dp)
//        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = condition,
                fontSize = 24.sp
            )
            Text(
                text = "$temp°",
                fontSize = 60.sp
            )
            Text(
                text = "Feels like $feelsLikeTemp°",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainPagePreview() {
    WeatherTheme {
        val data = WeatherUIState.Success(
            data = WeatherData(
                coordinates = OneCallCoordinates(
                    name = "Tehran",
                    lat = 0.0,
                    lon = 0.0,
                    timezone = "tehran",
                    timezone_offset = 0
                ),
                current = Current(
                    clouds = 27,
                    dew_point = 273.46,
                    dt = 1674649142,
                    feels_like = 286.08,
                    humidity = 38,
                    pressure = 1017,
                    sunrise = 1674617749,
                    sunset = 1674655697,
                    temp = 287.59,
                    uvi = 0.91,
                    visibility = 10000,
                    wind_deg = 246,
                    wind_gust = 1.71,
                    wind_speed = 2.64,
                    weather = emptyList()
                ),
                daily = emptyList(),
                hourly = emptyList()
            )
        )
        WeatherForecastScreen(weatherUIState = data,
            onNavigateToManageLocations = {})
    }
}

@Composable
@Preview(showBackground = true)
fun CurrentWeatherPreview() {
    WeatherTheme {
        CurrentTempAndCondition(temp = "5", feelsLikeTemp = "3", icon = "02d", condition = "Snow")
    }
}

@Composable
@Preview(showBackground = true)
fun FourDayPreview() {
    WeatherTheme {
        Text(text = "hello")
//        DailyForecastItem()
    }
}




